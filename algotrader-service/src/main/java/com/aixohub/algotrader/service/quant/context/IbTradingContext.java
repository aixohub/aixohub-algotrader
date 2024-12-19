package com.aixohub.algotrader.service.quant.context;

import com.aixohub.algotrader.service.quant.config.ContractBuilder;
import com.aixohub.algotrader.service.quant.exception.NoOrderAvailable;
import com.aixohub.algotrader.service.quant.exception.PriceNotAvailableException;
import com.aixohub.algotrader.service.quant.model.MarketDataRow;
import com.aixohub.algotrader.service.quant.observers.AccountObserver;
import com.aixohub.algotrader.service.quant.observers.HistoryObserver;
import com.aixohub.algotrader.service.quant.observers.MarketDataObserver;
import com.aixohub.algotrader.service.quant.observers.OrderObserver;
import com.aixohub.algotrader.service.quant.observers.impl.IbAccountObserver;
import com.aixohub.algotrader.service.quant.observers.impl.IbHistoryObserver;
import com.aixohub.algotrader.service.quant.observers.impl.IbMarketDataObserver;
import com.aixohub.algotrader.service.quant.observers.impl.IbOrderObserver;
import com.aixohub.algotrader.service.trading.lib.backtest.SimpleClosedOrder;
import com.aixohub.algotrader.service.trading.lib.backtest.SimpleOrder;
import com.aixohub.algotrader.service.trading.lib.model.ClosedOrder;
import com.aixohub.algotrader.service.trading.lib.model.Order;
import com.aixohub.algotrader.service.trading.lib.series.DoubleSeries;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.OrderType;
import com.ib.client.TickType;
import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Interactive Brokers trading context.
 */
public class IbTradingContext implements TradingContext {

    private static final Logger log = LoggerFactory.getLogger(IbTradingContext.class);
    private final ApiController controller;
    private final ContractBuilder contractBuilder;
    private List<String> contracts;
    private Map<String, Contract> ibContracts;
    private Map<String, Order> ibOrders;
    private Map<String, Map<TickType, Double>> contractPrices;
    private Map<String, MarketDataObserver> observers;
    private OrderType orderType;
    private AtomicInteger orderId = new AtomicInteger(0);
    private double availableFunds;
    private double netValue;
    private int leverage;
    private Connection connection;

    private IbTradingContext(ApiController controller, ContractBuilder contractBuilder, int leverage,
                             OrderType orderType) {
        this.controller = controller;
        this.contractBuilder = contractBuilder;
        this.leverage = leverage;
        this.orderType = orderType;
    }

    public IbTradingContext(ApiController controller, ContractBuilder contractBuilder,
                            OrderType orderType, int leverage)
            throws ClassNotFoundException, SQLException {
        this(controller, contractBuilder, leverage, orderType);
        this.contracts = Lists.newArrayList();
        this.contractPrices = Maps.newConcurrentMap();

        this.observers = Maps.newConcurrentMap();
        this.ibContracts = Maps.newConcurrentMap();
        this.ibOrders = Maps.newConcurrentMap();
        AccountObserver accountObserver = new IbAccountObserver(this);
        ((IbAccountObserver) accountObserver)
                .observableCashBalance().subscribe(aDouble -> availableFunds = aDouble);
        ((IbAccountObserver) accountObserver)
                .observableNetValue().subscribe(aDouble -> netValue = aDouble);
        controller.reqAccountUpdates(true, "", accountObserver);
    }

    public IbTradingContext(ApiController controller, ContractBuilder contractBuilder,
                            OrderType orderType, Connection connection, int leverage)
            throws ClassNotFoundException, SQLException {
        this(controller, contractBuilder, orderType, leverage);
        this.connection = connection;
    }

    @Override
    public double getLastPrice(String contract) throws PriceNotAvailableException {
        checkArgument(contract != null, "contract is null");
        if (!contractPrices.containsKey(contract) ||
                !contractPrices.get(contract).containsKey(TickType.ASK)) {
            throw new PriceNotAvailableException();
        }
        double price = contractPrices.get(contract).get(TickType.ASK);
        if (connection != null) {
            try {
                String sql = "INSERT INTO quotes (symbol, price) VALUES (?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, contract);
                stmt.setDouble(2, price);
                stmt.execute();

            } catch (SQLException e) {
                log.error("Could not insert record into database: " + contract + " - " + price, e);
            }

        }
        return contractPrices.get(contract).get(TickType.ASK);
    }

    public double getLastPrice(String contract, TickType tickType) throws PriceNotAvailableException {
        checkArgument(contract != null, "contract is null");
        checkArgument(tickType != null, "tickType is null");
        if (!contractPrices.containsKey(contract) || !contractPrices.get(contract).
                containsKey(tickType)) {
            throw new PriceNotAvailableException();
        }
        return contractPrices.get(contract).get(tickType);
    }

    @Override
    public void addContract(String contractSymbol) {
        contracts.add(contractSymbol);

        IbMarketDataObserver marketDataObserver = new IbMarketDataObserver(contractSymbol);
        observers.put(contractSymbol, marketDataObserver);

        Contract contract = contractBuilder.build(contractSymbol);
        ibContracts.put(contractSymbol, contract);

        controller.reqTopMktData(contract, "", false, true, marketDataObserver);

        marketDataObserver.priceObservable().subscribe(new Subscriber<MarketDataRow>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onNext(MarketDataRow price) {
                if (contractPrices.containsKey(contractSymbol)) {
                    contractPrices.get(contractSymbol).put(price.getTickType(), price.getPrice());
                } else {
                    Map<TickType, Double> map = Maps.newConcurrentMap();
                    map.put(price.getTickType(), price.getPrice());
                    contractPrices.put(contractSymbol, map);
                }

            }
        });
    }

    @Override
    public void removeContract(String contractSymbol) {
        contracts.remove(contractSymbol);
        contractPrices.remove(contractSymbol);
        ibContracts.remove(contractSymbol);
        controller.cancelTopMktData(observers.get(contractSymbol));
    }

    @Override
    public List<String> getContracts() {
        return contracts;
    }

    @Override
    public double getAvailableFunds() {
        return availableFunds;
    }

    @Override
    public double getNetValue() {
        return netValue;
    }

    @Override
    public double getLeverage() {
        return leverage;
    }

    @Override
    public MarketDataObserver getObserver(String contractSymbol) {
        checkArgument(contractSymbol != null, "contractSymbol is null");

        return observers.get(contractSymbol);
    }

    @Override
    public Order order(String contractSymbol, boolean buy, int amount)
            throws PriceNotAvailableException {
        checkArgument(contractSymbol != null, "contractSymbol is null");

        Order order = new IbOrder(
                orderId.getAndIncrement(),
                contractSymbol,
                Instant.now(),
                getLastPrice(contractSymbol),
                buy ? amount : -amount,
                submitIbOrder(contractSymbol, buy, amount, getLastPrice(contractSymbol))
        );

        ibOrders.put(contractSymbol, order);

        return order;
    }

    @Override
    public ClosedOrder close(Order order) throws PriceNotAvailableException {
        checkArgument(order != null, "order is null");

        log.debug("Amount taken from {} order that isLong {} : {}", order.getInstrument(),
                order.isLong(),
                order.getAmount());

        IbClosedOrder closedOrder = new IbClosedOrder(
                (SimpleOrder) order,
                Instant.now(),
                getLastPrice(order.getInstrument()),
                submitIbOrder(
                        order.getInstrument(),
                        order.isShort(),
                        order.getAmount(),
                        getLastPrice(order.getInstrument())));

        ibOrders.remove(order.getInstrument());
        ibOrders.put(order.getInstrument(), closedOrder);

        return closedOrder;

    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Order getLastOrderBySymbol(String symbol) throws NoOrderAvailable {
        checkArgument(symbol != null, "symbol is null");
        if (!ibOrders.containsKey(symbol)) {
            throw new NoOrderAvailable();
        }
        return ibOrders.get(symbol);
    }

    public DoubleSeries getHistory(String symbol, int daysOfHistory) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        String date = LocalDateTime.now().format(formatter);

        ContractBuilder contractBuilder = new ContractBuilder();

        Contract contract = contractBuilder.build(symbol);
        HistoryObserver historyObserver = new IbHistoryObserver(symbol);
        controller.reqHistoricalData(contract, date, daysOfHistory, Types.DurationUnit.DAY,
                Types.BarSize._1_min, Types.WhatToShow.TRADES, false, true,  historyObserver);
        return ((IbHistoryObserver) historyObserver).observableDoubleSeries()
                .toBlocking()
                .first();

    }

    public DoubleSeries getHistoryInMinutes(String symbol, int numberOfMinutes) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        String date = LocalDateTime.now().format(formatter);
        ContractBuilder contractBuilder = new ContractBuilder();

        Contract contract = contractBuilder.build(symbol);
        HistoryObserver historyObserver = new IbHistoryObserver(symbol);
        controller.reqHistoricalData(contract, date, numberOfMinutes * 60, Types.DurationUnit.SECOND,
                Types.BarSize._1_min, Types.WhatToShow.TRADES, false, true, historyObserver);

        DoubleSeries history = ((IbHistoryObserver) historyObserver).observableDoubleSeries()
                .toBlocking()
                .first();
        // We might need to pull history for last day if time of request is after market is closed
        if (history.size() == 0 || history.size() < numberOfMinutes) {
            controller.reqHistoricalData(contract, date, 1, Types.DurationUnit.DAY,
                    Types.BarSize._1_min, Types.WhatToShow.TRADES, false, true, historyObserver);

            history = ((IbHistoryObserver) historyObserver).observableDoubleSeries()
                    .toBlocking()
                    .first();

            return history.tail(numberOfMinutes);
        }

        return history;
    }

    private Observable<OrderState> submitIbOrder(String contractSymbol, boolean buy, int amount,
                                                 double price) {
        com.ib.client.Order ibOrder = new com.ib.client.Order();

        if (buy) {
            ibOrder.action(Types.Action.BUY);
        } else {
            ibOrder.action(Types.Action.SELL);
            amount = -amount;
        }

        ibOrder.orderType(orderType);
        if (orderType == OrderType.LMT) {
            ibOrder.lmtPrice(price);
        }
        ibOrder.totalQuantity(new Decimal(amount));

        OrderObserver orderObserver = new IbOrderObserver();
        log.debug("Sending order for {} in amount of {}", contractSymbol, amount);

        controller.placeOrModifyOrder(ibContracts.get(contractSymbol), ibOrder, orderObserver);

        return ((IbOrderObserver) orderObserver).observableOrderState();
    }

    @Override
    public double getChangeBySymbol(String symbol) throws PriceNotAvailableException {

        double closePrice = getLastPrice(symbol, TickType.CLOSE);
        double currentPrice = getLastPrice(symbol);

        BigDecimal diff = BigDecimal.valueOf(currentPrice).add(BigDecimal.valueOf(-closePrice));

        BigDecimal res = diff.multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(closePrice), RoundingMode.HALF_UP);
        BigDecimal rounded = res.setScale(2, RoundingMode.HALF_UP);
        return rounded.doubleValue();

    }

    public double getChangeBySymbol(String symbol, double price) throws PriceNotAvailableException {

        double closePrice = getLastPrice(symbol, TickType.CLOSE);

        BigDecimal diff = BigDecimal.valueOf(price).add(BigDecimal.valueOf(-closePrice));

        BigDecimal res = diff.multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(closePrice), RoundingMode.HALF_UP);
        BigDecimal rounded = res.setScale(2, RoundingMode.HALF_UP);
        return rounded.doubleValue();

    }

    public class IbOrder extends SimpleOrder {

        private OrderStatus orderStatus;

        public IbOrder(int id,
                       String contractSymbol,
                       Instant openInstant,
                       double openPrice,
                       int amount,
                       Observable<OrderState> observableOrderState) {
            super(id, contractSymbol, openInstant, openPrice, amount);
            this.orderStatus = OrderStatus.Inactive;
            observableOrderState.subscribe(newOrderState -> orderStatus = newOrderState.status());
            log.info("{} OPEN order in amount of {} at price {}", contractSymbol, amount, openPrice);
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

    }

    public class IbClosedOrder extends SimpleClosedOrder {

        private OrderStatus orderStatus;

        public IbClosedOrder(SimpleOrder simpleOrder,
                             Instant closeInstant,
                             double closePrice,
                             Observable<OrderState> observableOrderState) {
            super(simpleOrder, closePrice, closeInstant);
            this.orderStatus = OrderStatus.Inactive;
            observableOrderState.subscribe(newOrderState -> {
                orderStatus = newOrderState.status();
                if (newOrderState.status() == OrderStatus.Filled) {
                    ibOrders.remove(simpleOrder.getInstrument());
                }
            });
            log.info("{} CLOSE order in amount of {} at price {}",
                    simpleOrder.getInstrument(), -simpleOrder.getAmount(), closePrice);
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

    }

}

package com.aixohub.algotrader.broker.ib;

import com.aixohub.algotrader.base.model.AccountInfo;
import com.aixohub.algotrader.base.model.OrderInfo;
import com.aixohub.algotrader.base.utils.JsonUtils;
import com.aixohub.algotrader.broker.ib.model.CompletedOrder;
import com.aixohub.algotrader.broker.ib.model.OrderRow;
import com.aixohub.algotrader.broker.ib.model.PositionInfo;
import com.aixohub.algotrader.broker.ib.config.IConnectionConfiguration;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IBBroker {

    private static final Logger logger = LoggerFactory.getLogger(IBBroker.class);

    private IBStore ibStore;

    private BigDecimal startingCash = BigDecimal.ZERO;
    private BigDecimal cash = BigDecimal.ZERO;
    private BigDecimal startingValue = BigDecimal.ZERO;
    private BigDecimal value = BigDecimal.ZERO;

    private final Lock lockOrders = new ReentrantLock();
    private final Map<Integer, Order> orderById = new HashMap<>();
    private final Map<String, Execution> executions = new HashMap<>();
    private final Map<Integer, Map<Integer, OrderStatus>> orderStatxus = new HashMap<>();
    private final Queue<OrderInfo> notifications = new ConcurrentLinkedQueue<>();
    private final Deque<Integer> toNotify = new ArrayDeque<>();


    public IBBroker() {
        this.ibStore = new IBStore();
    }

    public void start(IConnectionConfiguration connect) {
        ibStore.connected(connect);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.warn("start-InterruptedException", e);
            throw new RuntimeException(e);
        }
        if (ibStore.isConnected()) {
            final CountDownLatch latch = new CountDownLatch(2);
            ibStore.setLatch(latch);
            ibStore.reqAccountUpdates(true, "");
            ibStore.reqPositions();
            try {
                latch.await(); // 等待数据下载完成
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            startingCash = cash = getCash("");
            startingValue = value = getValue("");
        } else {
            startingCash = cash = BigDecimal.ZERO;
            startingValue = value = BigDecimal.ZERO;
        }
    }

    public void stop() {
        ibStore.disconnected();
    }

    public Map<String, List<AccountInfo>> getAccMap() {
        return ibStore.getAccMap();
    }


    public BigDecimal getCash(String account) {
        cash = ibStore.getAccountCash(account);
        logger.info("getAccountCash-account: {}, cash: {}", account, cash);
        return cash;
    }

    public BigDecimal getValue(String account) {
        value = ibStore.getAccountValue(account);
        logger.info("getAccountValue-getValue: {}, value: {}", account, value);
        return value;
    }

    public PositionInfo getPosition(String symbol) {
        List<PositionInfo> positionList = ibStore.getPosition(symbol);
        logger.info("getPosition: " + JsonUtils.toJson(positionList));
        if (CollectionUtils.isNotEmpty(positionList)) {
            Map<String, PositionInfo> positionMap = positionList.stream().
                    collect(Collectors.toMap(t -> t.getContract().getSymbol(), Function.identity(), (v1, v2) -> v2));
            return positionMap.get(symbol);
        }
        return null;
    }

    public List<OrderRow> reqLiveOrders() {
        return ibStore.reqLiveOrders();
    }

    public List<CompletedOrder> reqCompletedOrders() {
        return ibStore.reqCompletedOrders();
    }
//
//    public void cancel(Order order) {
//        int orderId = order.getOrderId();
//        Order o = orderById.get(orderId);
//        if (o == null || o.getStatus() == Order.Status.CANCELLED) {
//            return;
//        }
//        ib.cancelOrder(orderId);
//    }
//
//    public Order.Status getOrderStatus(Order order) {
//        int orderId = order.getOrderId();
//        return orderById.getOrDefault(orderId, order).getStatus();
//    }
//
//    public Order submit(Order order) {
//        order.submit(this);
//
//        if (order.getOco() == null) {
//            order.setOcaGroup(UUID.randomUUID().toString());
//        } else {
//            int ocoOrderId = order.getOco().getOrderId();
//            order.setOcaGroup(orderById.get(ocoOrderId).getOcaGroup());
//        }
//
//        orderById.put(order.getOrderId(), order);
// //       ib.placeOrder(order);
//        notify(order);
//
//        return order;
//    }


//
//    public Order makeOrder(String action, Strategy owner, Data data, String symbol, double size, double price, Double priceLimit,
//                           ExecutionType execType, Validity valid, int tradeId, Map<String, Object> params) {
//
//        int orderId = ib.nextOrderId();
//        Order order = new IBOrder(action, owner, data, symbol, size, price, priceLimit, execType, valid, tradeId, ib.getClientId(), orderId, params);
//        order.addCommissionInfo(getCommissionInfo(data));
//        return order;
//    }
//
//    public Order buy(Strategy owner, Data data, String symbol, double size, Double price, Double priceLimit, ExecutionType execType,
//                     Validity valid, int tradeId, Map<String, Object> params) {
//        Order order = makeOrder("BUY", owner, data, symbol, size, price, priceLimit, execType, valid, tradeId, params);
//        return submit(order);
//    }
//
//    public Order sell(Strategy owner, Data data, String symbol, double size, Double price, Double priceLimit, ExecutionType execType,
//                      Validity valid, int tradeId, Map<String, Object> params) {
//        Order order = makeOrder("SELL", owner, data, symbol, size, price, priceLimit, execType, valid, tradeId, params);
//        return submit(order);
//    }


}

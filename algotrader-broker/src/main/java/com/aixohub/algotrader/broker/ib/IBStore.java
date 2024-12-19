package com.aixohub.algotrader.broker.ib;

import com.aixohub.algotrader.base.model.AccountInfo;
import com.aixohub.algotrader.broker.ib.config.IConnectionConfiguration;
import com.aixohub.algotrader.broker.ib.handler.CompletedOrdersHandler;
import com.aixohub.algotrader.broker.ib.handler.DefaultAccountHandler;
import com.aixohub.algotrader.broker.ib.handler.DefaultLiveOrderHandler;
import com.aixohub.algotrader.broker.ib.handler.DefaultPositionHandler;
import com.aixohub.algotrader.broker.ib.handler.DefaultTickByTickDataHandler;
import com.aixohub.algotrader.broker.ib.handler.DefaultTimeHandler;
import com.aixohub.algotrader.broker.ib.model.CompletedOrder;
import com.aixohub.algotrader.broker.ib.model.OrderRow;
import com.aixohub.algotrader.broker.ib.model.PositionInfo;
import com.ib.client.Contract;
import com.ib.controller.ApiController;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class IBStore implements ApiController.IConnectionHandler {
    private static final Logger logger = LoggerFactory.getLogger(IBStore.class);

    private final org.slf4j.Logger m_inLogger = logger;
    private final org.slf4j.Logger m_outLogger = logger;

    private static final int REQIDBASE = 0x01000000;

    private IConnectionConfiguration connect;

    private static IBStore instance;
    private static final Object lockInstance = new Object();

    CountDownLatch latch;

    private final Lock lockQueue = new ReentrantLock();
    private final Lock lockAccountUpdates = new ReentrantLock();
    private final Lock lockPositions = new ReentrantLock();
    private final Lock lockNotifications = new ReentrantLock();
    private final Lock lockTimeOffset = new ReentrantLock();

    private boolean dontReconnect = false;
    private boolean portUpdate = false;

    private Duration timeOffset = Duration.ZERO;
    private final List<String> managedAccountList = new ArrayList<>();

    private  List<PositionInfo> positionList = new ArrayList<>();

    Map<String, List<AccountInfo>> accMap = new ConcurrentHashMap<>();
    private Map<String, BigDecimal> accountCashMap = new ConcurrentHashMap<>();
    private Map<String, BigDecimal> accountValueMap = new ConcurrentHashMap<>();

    public List<PositionInfo> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<PositionInfo> positionList) {
        this.positionList = positionList;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public Map<String, List<AccountInfo>> getAccMap() {
        return accMap;
    }

    public void setAccMap(Map<String, List<AccountInfo>> accMap) {
        this.accMap = accMap;
    }

    public Map<String, BigDecimal> getAccountCashMap() {
        return accountCashMap;
    }

    public void setAccountCashMap(Map<String, BigDecimal> accountCashMap) {
        this.accountCashMap = accountCashMap;
    }

    public Map<String, BigDecimal> getAccountValueMap() {
        return accountValueMap;
    }

    public void setAccountValueMap(Map<String, BigDecimal> accountValueMap) {
        this.accountValueMap = accountValueMap;
    }

    private final Queue<Object> notifications = new LinkedList<>();
    private final Iterator<Integer> tickerIdIterator = new Iterator<>() {
        private int current = REQIDBASE;

        @Override
        public boolean hasNext() {
            return true; // Infinite sequence
        }

        @Override
        public Integer next() {
            return current++;
        }
    };

    private ApiController m_controller;
    private boolean debug;

    Logger getInLogger() {
        return m_inLogger;
    }

    Logger getOutLogger() {
        return m_outLogger;
    }

    public void initController() {
        if (m_controller == null) {
            m_controller = new ApiController(this, getInLogger(), getOutLogger());
        }
    }

    IBStore() {
        initController();
    }

    public static IBStore getInstance() {
        if (instance == null) {
            synchronized (lockInstance) {
                if (instance == null) {
                    instance = new IBStore();
                }
            }
        }
        return instance;
    }


    @Override
    public void connected(IConnectionConfiguration connect) {
        this.connect = connect;
        m_controller.connect(connect.getDefaultHost(), connect.getDefaultPort(),
                connect.getDefaultClientId(),
                connect.getDefaultConnectOptions() != null ? "" : null);
    }

    public boolean isConnected() {
        try {
            return m_controller.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void disconnected() {
        try {
            if (m_controller != null) {
                m_controller.disconnect();
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void accountList(List<String> list) {
        managedAccountList.addAll(list);
    }

    @Override
    public void error(Exception e) {

    }

    @Override
    public void message(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {

    }

    @Override
    public void show(String string) {

    }


    public void reConnect(boolean fromStart, boolean resubscribe) {
        boolean firstConnect = false;
        if (dontReconnect) return;

        try {
            if (isConnected()) {
                if (resubscribe) {
                    startDatas();
                }
                return;
            }
        } catch (Exception e) {
            firstConnect = true;
        }

        int retries = 3; // Example retry count, replace with parameter

        while (retries-- > 0 || retries < 0) {
            try {
                if (!firstConnect) {
                    Thread.sleep(3000); // Timeout between retries
                }
                firstConnect = false;
                connected(this.connect);
                if (isConnected()) {
                    if (!fromStart || resubscribe) {
                        startDatas();
                    }
                    return;
                }
            } catch (Exception e) {
                logger.warn("ib-Reconnect failed", e);
            }
        }

        dontReconnect = true;
    }

    private void startDatas() {
        // Re-kickstart data subscriptions
    }

    public void reqAccountUpdates(boolean subscribe, String account) {
        DefaultAccountHandler defaultAccountHandler = new DefaultAccountHandler(this);
        if (StringUtils.isBlank(account)) {
            if (CollectionUtils.isNotEmpty(this.managedAccountList)) {
                for (String managedAccount : this.managedAccountList) {
                    m_controller.reqAccountUpdates(subscribe, managedAccount, defaultAccountHandler);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        logger.warn("reqAccountUpdates-InterruptedException", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            CompletableFuture.runAsync(
                    () -> {
                        m_controller.reqAccountUpdates(subscribe, account, defaultAccountHandler);
                    });
        }
    }

    public void reqPositions() {
        m_controller.reqPositions(new DefaultPositionHandler(this));
    }

    public BigDecimal getAccountCash(String account) {
        // Lock access to accCash to avoid an event interfering
        if (StringUtils.isBlank(account)) {
            if (managedAccountList == null || managedAccountList.isEmpty()) {
                return BigDecimal.ZERO;
            }

            if (managedAccountList.size() > 1) {
                BigDecimal total = BigDecimal.ZERO;
                for (Map.Entry<String, BigDecimal> entry : accountCashMap.entrySet()) {
                    BigDecimal val = entry.getValue();
                    if (val != null) {
                        total = total.add(val);
                    }
                }
                return total;
            }

            // Only 1 account, fall through to return only 1
            account = managedAccountList.get(0);
        }

        // Return cash for specific account
        BigDecimal cashValue = accountCashMap.get(account);
        return cashValue != null ? cashValue : BigDecimal.ZERO;
    }

    /**
     * Returns the net liquidation value sent by TWS during regular updates
     * Waits for at least 1 successful account update download
     * <p>
     * If account is null, returns total value across all accounts
     * If account is specified or only 1 account exists,
     * returns value for that specific account
     */
    public BigDecimal getAccountValue(String account) {

        if (StringUtils.isBlank(account)) {
            if (managedAccountList == null || managedAccountList.isEmpty()) {
                return BigDecimal.ZERO;
            }

            if (managedAccountList.size() > 1) {
                BigDecimal total = BigDecimal.ZERO;
                for (Map.Entry<String, BigDecimal> entry : accountValueMap.entrySet()) {
                    BigDecimal value = entry.getValue();
                    if (value != null) {
                        total = total.add(value);
                    }
                }
                return total;
            }

            // Only 1 account, fall through to return only 1
            account = managedAccountList.get(0);
        }

        // Return value for specific account
        return accountValueMap.getOrDefault(account, BigDecimal.ZERO);

    }

    public long reqCurrentTime(){
        DefaultTimeHandler defaultTimeHandler = new DefaultTimeHandler();
        m_controller.reqCurrentTime(defaultTimeHandler);
        return defaultTimeHandler.getTime();
    }


    public List<PositionInfo> getPosition(String symbol){
        List<PositionInfo> positionList = getPositionList();
        return positionList;
    }

    public  List<OrderRow> reqLiveOrders(){
        DefaultLiveOrderHandler defaultLiveOrderHandler = new DefaultLiveOrderHandler();
        m_controller.reqLiveOrders(defaultLiveOrderHandler);
        return defaultLiveOrderHandler.getmOrderList();
    }

    public  List<CompletedOrder> reqCompletedOrders(){
        CompletedOrdersHandler completedOrdersHandler = new CompletedOrdersHandler();
        m_controller.reqCompletedOrders(completedOrdersHandler);
        return completedOrdersHandler.getCompletedOrders();
    }

    public void reqTickByTickData(String symbol){
        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.localSymbol(symbol);
        contract.secType("STK");
        contract.currency("USD");
        contract.exchange("SMART");
        String tickType = "BidAsk";
        int numberOfTicks = 2;
        boolean ignoreSize = false;
        DefaultTickByTickDataHandler defaultTickByTickDataHandler = new DefaultTickByTickDataHandler();
        m_controller.reqTickByTickData(contract, tickType, numberOfTicks, ignoreSize, defaultTickByTickDataHandler);
    }
    public void placeOrModifyOrder(){

      //  m_controller.placeOrModifyOrder();
    }

    /**
     *
     */
    public void cancelAllOrders() {
        m_controller.cancelAllOrders();
    }


}

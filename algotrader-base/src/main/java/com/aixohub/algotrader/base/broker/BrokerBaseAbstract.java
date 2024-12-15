package com.aixohub.algotrader.base.broker;


import com.aixohub.algotrader.base.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BrokerBaseAbstract {

    protected Map<String, CommissionInfo> commInfo = new HashMap<>();

    public BrokerBaseAbstract() {
        init();
    }

    protected void init() {
        if (!commInfo.containsKey(null)) {
            commInfo.put(null, new CommissionInfo(true));
        }
    }

    public void start() {
        init();
    }

    public void stop() {
        // Override as needed
    }

    public abstract void addOrderHistory(List<OrderInfo> orders, boolean notify);

    public abstract void setFundHistory(FundInfo fund);

    public CommissionInfo getCommissionInfo(Data data) {
        return commInfo.getOrDefault(data.getName(), commInfo.get(null));
    }

    public void setCommission(double commission, Double margin, double mult,
                              String commType, boolean percabs, boolean stocklike,
                              double interest, boolean interestLong, double leverage,
                              boolean autoMargin, String name) {

        CommissionInfo comm = new CommissionInfo(commission, margin, mult, commType,
                stocklike, percabs, interest, interestLong,
                leverage, autoMargin);
        commInfo.put(name, comm);
    }

    public void addCommissionInfo(CommissionInfo commInfo, String name) {
        this.commInfo.put(name, commInfo);
    }

    public abstract double getCash();

    public abstract double getValue(List<Data> datas);

    public double getFundShares() {
        return 1.0;
    }

    public double getFundValue() {
        return getValue(null);
    }

    public void setFundMode(boolean fundMode, Double fundStartVal) {
        // Override as needed
    }

    public boolean getFundMode() {
        return false; // Default mode
    }

    public abstract PositionModel getPosition(Data data);

    public abstract OrderInfo submit(OrderInfo order);

    public abstract void cancel(OrderInfo order);

    public abstract OrderInfo buy(OwnerInfo owner, Data data, double size, Double price,
                                  Double priceLimit, String execType, String valid,
                                  int tradeId, Map<String, Object> kwargs);

    public abstract OrderInfo sell(OwnerInfo owner, Data data, double size, Double price,
                                   Double priceLimit, String execType, String valid,
                                   int tradeId, Map<String, Object> kwargs);


    public void next() {
        // Override as needed
    }


    public static class Data {
        public String getName() {
            return "default"; // Replace with actual implementation
        }

        // Implementation
    }

}

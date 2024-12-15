package com.aixohub.algotrader.base.model;

public class CommissionInfo {
    private boolean percabs;

    public CommissionInfo(boolean percabs) {
        this.percabs = percabs;
    }

    public CommissionInfo(double commission, Double margin, double mult, String commType,
                          boolean stocklike, boolean percabs, double interest, boolean interestLong,
                          double leverage, boolean autoMargin) {
        // Initialize all fields
        this.percabs = percabs;
    }
}

package com.aixohub.algotrader.service.rule;

public class MarketCondition {
    private double volatility;

    private String trend;


    public MarketCondition(double volatility, String trend) {
        this.volatility = volatility;
        this.trend = trend;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }


    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    }

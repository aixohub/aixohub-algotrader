package com.aixohub.algotrader.service.rule;

public class TradingStrategyContext {
    private TradingStrategy strategy;

    public void setStrategy(TradingStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy() {
        if (strategy != null) {
            strategy.execute();
        } else {
            System.out.println("No strategy is set.");
        }
    }
}
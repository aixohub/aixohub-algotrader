package com.aixohub.algotrader.service.rule;

public interface TradingStrategy {

    void execute(MarketData marketData);
}
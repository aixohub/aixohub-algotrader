package com.aixohub.algotrader.service.rule.impl;

import com.aixohub.algotrader.service.rule.MarketData;
import com.aixohub.algotrader.service.rule.TradingStrategy;

public class TrendFollowingStrategy implements TradingStrategy {
    @Override
    public void execute(MarketData marketData) {
        System.out.println("Executing Trend Following Strategy...");
        // 具体的交易逻辑
    }
}
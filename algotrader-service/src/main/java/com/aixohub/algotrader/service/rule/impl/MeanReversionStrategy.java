package com.aixohub.algotrader.service.rule.impl;

import com.aixohub.algotrader.service.rule.MarketData;
import com.aixohub.algotrader.service.rule.TradingStrategy;

public class MeanReversionStrategy implements TradingStrategy {
    @Override
    public void execute(MarketData marketData) {
        System.out.println("Executing Mean Reversion Strategy...");
        // 具体的交易逻辑
    }
}
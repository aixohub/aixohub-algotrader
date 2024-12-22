package com.aixohub.algotrader.service.rule;

import com.aixohub.algotrader.service.rule.impl.MeanReversionStrategy;
import com.aixohub.algotrader.service.rule.impl.TrendFollowingStrategy;

import java.util.HashMap;
import java.util.Map;

// 策略管理器
public class StrategyManager {
    private TradingStrategy currentStrategy;
    private final Map<String, TradingStrategy> strategies = new HashMap<>();

    public StrategyManager() {
        // 注册所有策略
        strategies.put("TrendFollowingStrategy", new TrendFollowingStrategy());
        strategies.put("MeanReversionStrategy", new MeanReversionStrategy());
    }

    public void switchTo(String strategyName) {
        if (strategies.containsKey(strategyName)) {
            currentStrategy = strategies.get(strategyName);
            System.out.println("Switched to strategy: " + strategyName);
        } else {
            throw new IllegalArgumentException("Strategy not found: " + strategyName);
        }
    }

    public void executeCurrentStrategy(MarketData marketData) {
        if (currentStrategy != null) {
            currentStrategy.execute(marketData);
        }
    }
}

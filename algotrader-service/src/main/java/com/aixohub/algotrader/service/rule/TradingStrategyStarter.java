package com.aixohub.algotrader.service.rule;

public class TradingStrategyStarter {
    public static void main(String[] args) {
        try {
            // 初始化 Drools
            RuleEngineManager manger = new RuleEngineManager("trade_strategy.drl");

            MarketCondition marketCondition = new MarketCondition(0.0, "uptrend");
            StrategyManager strategyManager = new StrategyManager();
            MarketData marketData = new MarketData();

            manger.evaluateRules(marketCondition,  strategyManager);
            strategyManager.executeCurrentStrategy(marketData);

            MarketCondition marketCondition1 = new MarketCondition(2, "uptrend");
            manger.evaluateRules(marketCondition1,  strategyManager);
            strategyManager.executeCurrentStrategy(marketData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
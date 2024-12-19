package com.aixohub.algotrader.service.trading.main.strategy;

import com.aixohub.algotrader.service.trading.lib.model.TradingContext;
import com.aixohub.algotrader.service.trading.lib.model.TradingStrategy;

import java.util.ArrayList;
import java.util.List;

public class MultipleTradingStrategy implements TradingStrategy {
    List<TradingStrategy> mStrategies = new ArrayList<>();

    public static MultipleTradingStrategy of(TradingStrategy... strategies) {
        MultipleTradingStrategy strategy = new MultipleTradingStrategy();
        for (TradingStrategy s : strategies) {
            strategy.add(s);
        }
        return strategy;
    }

    public boolean add(TradingStrategy strategy) {
        return mStrategies.add(strategy);
    }

    public int size() {
        return mStrategies.size();
    }

    @Override
    public void onStart(TradingContext context) {
        mStrategies.forEach(t -> t.onStart(context));
    }

    @Override
    public void onTick() {
        mStrategies.forEach(TradingStrategy::onTick);
    }

    @Override
    public void onEnd() {
        mStrategies.forEach(TradingStrategy::onEnd);
    }

    @Override
    public String toString() {
        return "MultipleStrategy{" +
                "mStrategies=" + mStrategies +
                '}';
    }
}

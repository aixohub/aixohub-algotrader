package com.aixohub.algotrader.service.quant.strategy;

import java.util.List;

/**
 * Runs strategy in defined trading context.
 */
public interface StrategyRunner {

    /**
     * Run specified {@link TradingStrategy} for symbols collection in given {@link TradingContext}.
     *
     * @param strategy strategy to run
     * @param symbols  list
     */
    void run(TradingStrategy strategy, List<String> symbols);

    /**
     * Stop the specified strategy for specified symbols.
     *
     * @param strategy strategy to stop
     * @param symbols  symbols list
     */
    void stop(TradingStrategy strategy, List<String> symbols);

}

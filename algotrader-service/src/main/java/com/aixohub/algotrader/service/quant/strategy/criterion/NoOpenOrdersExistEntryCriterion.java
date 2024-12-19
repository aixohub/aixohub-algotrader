package com.aixohub.algotrader.service.quant.strategy.criterion;


import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;
import com.aixohub.algotrader.service.quant.exception.NoOrderAvailable;
import com.aixohub.algotrader.service.quant.strategy.Criterion;
import com.aixohub.algotrader.service.trading.lib.model.Order;

import java.util.List;


/**
 * Checks that no open orders available for specified symbols
 */
public class NoOpenOrdersExistEntryCriterion implements Criterion {

    protected final List<String> symbols;
    protected final TradingContext tradingContext;

    public NoOpenOrdersExistEntryCriterion(TradingContext tradingContext, List<String> symbols) {
        this.tradingContext = tradingContext;
        this.symbols = symbols;
    }

    @Override
    public boolean isMet() throws CriterionViolationException {
        for (String symbol : symbols) {
            Order order = null;
            try {
                order = tradingContext.getLastOrderBySymbol(symbol);
                if (order != null) {
                    return false;
                }
            } catch (NoOrderAvailable e) {
                throw new RuntimeException(e);
            }

        }
        return true;
    }
}
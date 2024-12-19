package com.aixohub.algotrader.service.quant.strategy.criterion.common;


import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;
import com.aixohub.algotrader.service.quant.exception.NoOrderAvailable;
import com.aixohub.algotrader.service.quant.strategy.Criterion;
import com.ib.client.OrderStatus;

import java.util.List;

/**
 * Test if there are any orders that are in pending (not filled yet) state
 */
public class NoPendingOrdersCommonCriterion implements Criterion {

    private final TradingContext tradingContext;
    private final List<String> symbols;

    public NoPendingOrdersCommonCriterion(TradingContext tradingContext, List<String> symbols) {
        this.tradingContext = tradingContext;
        this.symbols = symbols;
    }

    @Override
    public boolean isMet() throws CriterionViolationException {
        for (String symbol : symbols) {
            try {
                if (tradingContext.getLastOrderBySymbol(symbol) != null
                        && tradingContext.getLastOrderBySymbol(symbol).getOrderStatus() != OrderStatus.Filled) {
                    return false;
                }
            } catch (NoOrderAvailable e) {

            }
        }
        return true;
    }
}

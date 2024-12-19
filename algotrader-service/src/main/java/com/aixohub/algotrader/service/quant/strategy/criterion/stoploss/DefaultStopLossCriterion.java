package com.aixohub.algotrader.service.quant.strategy.criterion.stoploss;


import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;
import com.aixohub.algotrader.service.quant.exception.NoOrderAvailable;
import com.aixohub.algotrader.service.quant.exception.PriceNotAvailableException;
import com.aixohub.algotrader.service.quant.strategy.Criterion;
import org.lst.trading.lib.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class DefaultStopLossCriterion implements Criterion {

    private static final Logger log = LoggerFactory.getLogger(DefaultStopLossCriterion.class);
    private final double thresholdAmount;
    private final TradingContext tradingContext;
    private final List<String> symbols;

    public DefaultStopLossCriterion(List<String> symbols, double thresholdAmount,
                                    TradingContext tradingContext) {
        this.tradingContext = tradingContext;
        this.thresholdAmount = thresholdAmount;
        this.symbols = symbols;
    }


    @Override
    public boolean isMet() throws CriterionViolationException {
        // check if there are open orders
        double totalPl = 0.0;
        for (String symbol : symbols) {
            try {
                Order order = tradingContext.getLastOrderBySymbol(symbol);
                double symbolPl = (tradingContext.getLastPrice(symbol) * order.getAmount())
                        + (order.getOpenPrice() * -order.getAmount());
                log.debug("Symbol P/L: {}", symbolPl);
                totalPl += symbolPl;
            } catch (NoOrderAvailable | PriceNotAvailableException noOrderAvailable) {
                return false;
            }
        }
        log.debug("Total PL: {}", totalPl);
        return totalPl <= thresholdAmount;
    }
}

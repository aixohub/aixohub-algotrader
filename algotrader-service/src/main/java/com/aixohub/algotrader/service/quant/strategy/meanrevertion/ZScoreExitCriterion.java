package com.aixohub.algotrader.service.quant.strategy.meanrevertion;


import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;
import com.aixohub.algotrader.service.quant.exception.NoOrderAvailable;
import com.aixohub.algotrader.service.quant.exception.PriceNotAvailableException;
import com.aixohub.algotrader.service.quant.strategy.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ZScoreExitCriterion implements Criterion {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZScoreExitCriterion.class);

    private final String firstSymbol;
    private final String secondSymbol;
    private ZScore zScore;
    private TradingContext tradingContext;
    private double exitZScore;

    public ZScoreExitCriterion(String firstSymbol, String secondSymbol,
                               ZScore zScore, TradingContext tradingContext) {
        this.zScore = zScore;
        this.firstSymbol = firstSymbol;
        this.secondSymbol = secondSymbol;
        this.tradingContext = tradingContext;
    }

    public ZScoreExitCriterion(String firstSymbol, String secondSymbol,
                               double exitZScore, ZScore zScore, TradingContext tradingContext) {
        this(firstSymbol, secondSymbol, zScore, tradingContext);
        this.exitZScore = exitZScore;
    }

    @Override
    public boolean isMet() throws CriterionViolationException {
        try {
            double zs =
                    zScore.get(
                            tradingContext.getLastPrice(firstSymbol), tradingContext.getLastPrice(secondSymbol));
            if (tradingContext.getLastOrderBySymbol(firstSymbol).isShort() && zs < exitZScore ||
                    tradingContext.getLastOrderBySymbol(firstSymbol).isLong() && zs > exitZScore) {
                return true;
            }
        } catch (PriceNotAvailableException | NoOrderAvailable e) {
            LOGGER.warn("ZScoreExitCriterion-error ", e);
            return false;
        }
        return false;
    }
}

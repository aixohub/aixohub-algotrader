package com.aixohub.algotrader.service.quant.strategy.meanrevertion;


import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;
import com.aixohub.algotrader.service.quant.exception.PriceNotAvailableException;
import com.aixohub.algotrader.service.quant.strategy.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ZScoreEntryCriterion implements Criterion {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZScoreEntryCriterion.class);

    private final String firstSymbol;
    private final String secondSymbol;
    private final double entryZScore;
    private ZScore zScore;
    private TradingContext tradingContext;

    public ZScoreEntryCriterion(String firstSymbol, String secondSymbol,
                                double entryZScore, ZScore zScore, TradingContext tradingContext) {
        this.zScore = zScore;
        this.firstSymbol = firstSymbol;
        this.secondSymbol = secondSymbol;
        this.tradingContext = tradingContext;
        this.entryZScore = entryZScore;
    }

    @Override
    public boolean isMet() throws CriterionViolationException {
        try {
            double zs =
                    zScore.get(
                            tradingContext.getLastPrice(firstSymbol), tradingContext.getLastPrice(secondSymbol));
            if (zs < -entryZScore || zs > entryZScore) {
                return true;
            }
        } catch (PriceNotAvailableException e) {
            LOGGER.warn("ZScoreEntryCriterion: ", e);
            return false;
        }
        return false;
    }
}

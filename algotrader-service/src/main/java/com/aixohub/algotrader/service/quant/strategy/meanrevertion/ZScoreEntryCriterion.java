package com.aixohub.algotrader.service.quant.strategy.meanrevertion;


import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;
import com.aixohub.algotrader.service.quant.exception.PriceNotAvailableException;
import com.aixohub.algotrader.service.quant.strategy.Criterion;

/**
 *
 */
public class ZScoreEntryCriterion implements Criterion {

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
            return false;
        }
        return false;
    }
}

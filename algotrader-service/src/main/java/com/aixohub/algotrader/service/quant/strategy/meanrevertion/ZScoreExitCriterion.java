package com.aixohub.algotrader.service.quant.strategy.meanrevertion;


import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;
import com.aixohub.algotrader.service.quant.exception.NoOrderAvailable;
import com.aixohub.algotrader.service.quant.exception.PriceNotAvailableException;
import com.aixohub.algotrader.service.quant.strategy.Criterion;

/**
 *
 */
public class ZScoreExitCriterion implements Criterion {

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
        } catch (PriceNotAvailableException e) {
            return false;
        } catch (NoOrderAvailable noOrderAvailable) {
            noOrderAvailable.printStackTrace();
            return false;
        }
        return false;
    }
}

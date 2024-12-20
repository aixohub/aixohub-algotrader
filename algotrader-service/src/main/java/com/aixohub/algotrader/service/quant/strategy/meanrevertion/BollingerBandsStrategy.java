package com.aixohub.algotrader.service.quant.strategy.meanrevertion;


import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.NoOrderAvailable;
import com.aixohub.algotrader.service.quant.exception.PriceNotAvailableException;
import com.aixohub.algotrader.service.quant.strategy.AbstractStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bollinger bands strategy
 */
public class BollingerBandsStrategy extends AbstractStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BollingerBandsStrategy.class);

    private final String firstSymbol;
    private final String secondSymbol;
    private final ZScore zScore;

    public BollingerBandsStrategy(String firstSymbol, String secondSymbol,
                                  TradingContext tradingContext, ZScore zScore) {

        super(tradingContext);
        this.firstSymbol = firstSymbol;
        this.secondSymbol = secondSymbol;
        this.zScore = zScore;
    }

    @Override
    public int getLotSize(String contract, boolean buy) {
        return 0;
    }

    @Override
    public void openPosition() throws PriceNotAvailableException {
        double hedgeRatio = Math.abs(zScore.getHedgeRatio());

        double baseAmount =
                (tradingContext.getNetValue() * 0.5 * Math.min(4, tradingContext.getLeverage()))
                        / (tradingContext.getLastPrice(secondSymbol) + hedgeRatio * tradingContext.getLastPrice
                        (firstSymbol));

        tradingContext.order(firstSymbol, zScore.getLastCalculatedZScore() < 0,
                Math.max((int) (baseAmount * hedgeRatio), 1));
        LOGGER.info("BollingerBandsStrategy-openPosition Order of {} in amount {}", firstSymbol, (int) (baseAmount * hedgeRatio));

        tradingContext.order(secondSymbol, zScore.getLastCalculatedZScore() > 0, (int) baseAmount);
        LOGGER.info("BollingerBandsStrategy-openPosition Order of {} in amount {}", secondSymbol, (int) baseAmount);
    }

    @Override
    public void closePosition() throws PriceNotAvailableException {
        try {
            tradingContext.close(tradingContext.getLastOrderBySymbol(firstSymbol));
        } catch (NoOrderAvailable e) {
            LOGGER.warn("BollingerBandsStrategy-closePosition-error firstSymbol:{} ", firstSymbol, e);
        }
        try {
            tradingContext.close(tradingContext.getLastOrderBySymbol(secondSymbol));
        } catch (NoOrderAvailable e) {
            LOGGER.warn("BollingerBandsStrategy-closePosition-error secondSymbol:{} ", secondSymbol, e);
        }
    }
}

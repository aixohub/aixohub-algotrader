package com.aixohub.algotrader.service.quant.config;

import com.aixohub.algotrader.service.quant.context.IbTradingContext;
import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.strategy.Criterion;
import com.aixohub.algotrader.service.quant.strategy.runner.IbPerMinuteStrategyRunner;
import com.aixohub.algotrader.service.quant.strategy.TradingStrategy;
import com.aixohub.algotrader.service.quant.strategy.StrategyRunner;
import com.aixohub.algotrader.service.quant.strategy.criterion.NoOpenOrdersExistEntryCriterion;
import com.aixohub.algotrader.service.quant.strategy.criterion.OpenIbOrdersExistForAllSymbolsExitCriterion;
import com.aixohub.algotrader.service.quant.strategy.criterion.common.NoPendingOrdersCommonCriterion;
import com.aixohub.algotrader.service.quant.strategy.criterion.stoploss.DefaultStopLossCriterion;
import com.aixohub.algotrader.service.quant.strategy.meanrevertion.BollingerBandsStrategy;
import com.aixohub.algotrader.service.quant.strategy.meanrevertion.ZScore;
import com.aixohub.algotrader.service.quant.strategy.meanrevertion.ZScoreEntryCriterion;
import com.aixohub.algotrader.service.quant.strategy.meanrevertion.ZScoreExitCriterion;
import com.aixohub.algotrader.service.quant.util.MathUtil;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.ib.client.OrderType;
import com.ib.controller.ApiController;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class Config extends AbstractModule {

    private final String host;
    private final int port;
    private final String symbolList;

    public Config(String host, int port, String symbolList) {
        this.host = host;
        this.port = port;
        this.symbolList = symbolList;
    }

    @Override
    protected void configure() {
        bind(StrategyRunner.class).to(IbPerMinuteStrategyRunner.class);
    }

    @Provides
    ApiController apiController() {
        ApiController controller =
                new ApiController(new IbConnectionHandler(), null, null);
        controller.connect(host, port, 0, null);
        return controller;
    }

    @Provides
    TradingContext tradingContext(ApiController controller) throws SQLException, ClassNotFoundException {
        return new IbTradingContext(
                controller,
                new ContractBuilder(),
                OrderType.MKT,
//        DriverManager.getConnection("jdbc:mysql://localhost/fx", "root", "admin"),
                2
        );
    }

    @Provides
    TradingStrategy strategy(TradingContext tradingContext) {
        List<String> contracts = Arrays.asList(symbolList.split(","));

        ZScore zScore = new ZScore(20, new MathUtil());

        TradingStrategy strategy = new BollingerBandsStrategy(
                contracts.get(0),
                contracts.get(1),
                tradingContext,
                zScore);

        Criterion zScoreEntryCriterion = new ZScoreEntryCriterion(contracts.get(0), contracts.get(1), 1, zScore,
                tradingContext);

        Criterion zScoreExitCriterion = new ZScoreExitCriterion(contracts.get(0), contracts.get(1), 0, zScore,
                tradingContext);

        Criterion noPendingOrdersCommonCriterion =
                new NoPendingOrdersCommonCriterion(tradingContext, contracts);

        Criterion noOpenOrdersExistCriterion =
                new NoOpenOrdersExistEntryCriterion(tradingContext, contracts);

        Criterion openOrdersExistForAllSymbolsCriterion =
                new OpenIbOrdersExistForAllSymbolsExitCriterion(tradingContext, contracts);

        Criterion stopLoss = new DefaultStopLossCriterion(contracts, -100, tradingContext);

        strategy.addCommonCriterion(noPendingOrdersCommonCriterion);

        strategy.addEntryCriterion(noOpenOrdersExistCriterion);
        strategy.addEntryCriterion(zScoreEntryCriterion);

        strategy.addExitCriterion(openOrdersExistForAllSymbolsCriterion);
        strategy.addEntryCriterion(zScoreExitCriterion);

        strategy.addStopLossCriterion(stopLoss);

        return strategy;

    }

}

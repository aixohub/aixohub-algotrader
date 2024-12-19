package com.aixohub.algotrader.service.quant.strategy;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Strategy runner that executes every minute
 */
public class IbPerMinuteStrategyRunner implements StrategyRunner {

    @Override
    public void run(TradingStrategy strategy, List<String> symbols) {

        for (String symbol : symbols) {
            strategy.addSymbol(symbol);
        }

        Timer timer = new Timer(true);
        DateTime dt = new DateTime();

        timer.schedule(
                new TriggerTick(strategy),
                new Date((dt.getMillis() - (dt.getSecondOfMinute() * 1000L)) + 59000),
                60000);

    }

    @Override
    public void stop(TradingStrategy strategy, List<String> symbols) {

    }

    private static class TriggerTick extends TimerTask {

        private final TradingStrategy strategy;

        TriggerTick(TradingStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public void run() {
            strategy.onTick();
        }

    }
}

package com.aixohub.algotrader.broker.ib.handler;

import com.ib.client.Decimal;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.client.TickByTick;
import com.ib.client.Util;
import com.ib.controller.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultTickByTickDataHandler implements ApiController.ITickByTickDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTickByTickDataHandler.class);

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size, TickAttribLast tickAttribLast, String exchange, String specialConditions) {
        logger.info("tickByTickAllLast reqId {}", reqId);
    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize, Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
        TickByTick tick = new TickByTick(time, bidPrice, bidSize, askPrice, askSize, tickAttribBidAsk);
        logger.info("tickByTickBidAsk reqId {}, time {}, bidPrice {}, bidSize {}, askPrice {}, askSize {}",
                reqId, Util.UnixSecondsToString(time, "yyyy-MM-dd HH:mm:ss"), bidPrice, bidSize, askPrice, askSize );
    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        logger.info("tickByTickMidPoint reqId {}", reqId);
    }

    @Override
    public void tickByTickHistoricalTickAllLast(int reqId, List<HistoricalTickLast> ticks) {
        logger.info("tickByTickHistoricalTickAllLast reqId {}", reqId);
    }

    @Override
    public void tickByTickHistoricalTickBidAsk(int reqId, List<HistoricalTickBidAsk> ticks) {
        logger.info("tickByTickHistoricalTickBidAsk reqId {}", reqId);

    }

    @Override
    public void tickByTickHistoricalTick(int reqId, List<HistoricalTick> ticks) {
        logger.info("tickByTickHistoricalTick reqId {}", reqId);

    }
}

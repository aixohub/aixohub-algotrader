package com.aixohub.algotrader.broker.ib.handler;

import com.ib.controller.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTimeHandler implements ApiController.ITimeHandler {

    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private static final Logger logger = LoggerFactory.getLogger(DefaultTimeHandler.class);

    @Override
    public void currentTime(long time) {
        logger.info("DefaultTimeHandler: {}", time);
        this.time = time;
    }
}

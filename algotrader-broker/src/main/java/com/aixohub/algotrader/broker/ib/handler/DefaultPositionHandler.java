package com.aixohub.algotrader.broker.ib.handler;

import com.aixohub.algotrader.broker.ib.IBStore;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.controller.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultPositionHandler implements ApiController.IPositionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPositionHandler.class);

    IBStore ibStore;

    public DefaultPositionHandler(IBStore ibStore) {
        this.ibStore = ibStore;
    }


    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        logger.info("DefaultPositionHandler-account:{} contract: {} value: {} currency: {}", account, contract, pos, avgCost);

    }

    @Override
    public void positionEnd() {

    }
}

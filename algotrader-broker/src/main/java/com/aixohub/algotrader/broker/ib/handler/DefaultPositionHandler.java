package com.aixohub.algotrader.broker.ib.handler;

import com.aixohub.algotrader.broker.ib.IBStore;
import com.aixohub.algotrader.broker.ib.model.ContractInfo;
import com.aixohub.algotrader.broker.ib.model.PositionInfo;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.controller.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultPositionHandler implements ApiController.IPositionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPositionHandler.class);

    List<PositionInfo> positionInfoList = new ArrayList<>();

    public List<PositionInfo> getPositionInfoList() {
        return positionInfoList;
    }

    IBStore ibStore;

    public DefaultPositionHandler(IBStore ibStore) {
        this.ibStore = ibStore;
    }


    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        logger.info("DefaultPositionHandler-account:{} contract: {} value: {} currency: {}", account, contract, pos, avgCost);

        ContractInfo info = new ContractInfo();
        info.setConid(contract.conid());
        info.setSymbol(contract.symbol());
        info.setSecType(contract.getSecType());
        info.setLastTradeDateOrContractMonth(contract.lastTradeDateOrContractMonth());
        info.setStrike(contract.strike());
        info.setRight(contract.getRight());
        info.setMultiplier(contract.multiplier());
        info.setExchange(contract.exchange());
        info.setPrimaryExch(contract.primaryExch());
        info.setCurrency(contract.currency());
        info.setLocalSymbol(contract.localSymbol());
        info.setTradingClass(contract.tradingClass());
        info.setIssuerId(contract.issuerId());

        info.setDescription(contract.description());
        info.setIncludeExpired(contract.includeExpired());
        PositionInfo positionInfo = new PositionInfo(account, info, pos, avgCost);
        positionInfoList.add(positionInfo);
    }

    @Override
    public void positionEnd() {
        ibStore.setPositionList(positionInfoList);
    }
}

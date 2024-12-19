package com.aixohub.algotrader.service.quant.config;

import com.aixohub.algotrader.broker.ib.config.IConnectionConfiguration;
import com.google.common.collect.Lists;
import com.ib.controller.ApiController.IConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class IbConnectionHandler implements IConnectionHandler {

    private static Logger logger = LoggerFactory.getLogger(IbConnectionHandler.class);
    private ArrayList<String> accountList = Lists.newArrayList();


    @Override
    public void connected(IConnectionConfiguration conn) {
        logger.info("Connected");
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void disconnected() {
        logger.info("Disconnected");
    }

    @Override
    public void accountList(List<String> list) {
        show("Received account list");
        accountList.clear();
        accountList.addAll(list);
    }


    @Override
    public void error(Exception e) {
        logger.warn("error: ", e);
    }

    @Override
    public void message(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        logger.info("Message id: {}, errorCode: {}, errorMsg: {}", id, errorCode, errorMsg);

    }

    @Override
    public void show(String string) {
        logger.info(string);
    }

    public ArrayList<String> getAccountList() {
        return accountList;
    }
}

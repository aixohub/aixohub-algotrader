package com.aixohub.algotrader.broker.ib.handler;

import com.aixohub.algotrader.broker.ib.model.CompletedOrder;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.controller.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrdersHandler implements ApiController.ICompletedOrdersHandler {

    private static final Logger logger = LoggerFactory.getLogger(CompletedOrdersHandler.class);

    List<CompletedOrder> completedOrders = new ArrayList<>();

    public List<CompletedOrder> getCompletedOrders() {
        return completedOrders;
    }

    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        CompletedOrder completedOrder = new CompletedOrder(contract, order, orderState);
        logger.info("completedOrder-- completedOrder:{}", completedOrder);
        completedOrders.add(completedOrder);
    }

    @Override
    public void completedOrdersEnd() {
        logger.info("completedOrdersEnd--");
    }
}

package com.aixohub.algotrader.broker.ib.handler;

import com.aixohub.algotrader.broker.ib.model.OrderRow;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultLiveOrderHandler implements ApiController.ILiveOrderHandler {

    private final Map<Integer, OrderRow> m_map = new HashMap<>();
    private final List<OrderRow> mOrderList = new ArrayList<>();

    public List<OrderRow> getmOrderList() {
        return mOrderList;
    }

    @Override
    public void openOrder(Contract contract, Order order, OrderState orderState) {
        OrderRow full = m_map.get(order.permId());
        if (full != null) {
            full.setM_order(order);
            full.setM_state(orderState);
        } else if (shouldAdd(contract, order, orderState)) {
            full = new OrderRow(contract, order, orderState);
            mOrderList.add(full);
            m_map.put(order.permId(), full);

        }
    }


    protected boolean shouldAdd(Contract contract, Order order, OrderState orderState) {
        return true;
    }

    @Override
    public void openOrderEnd() {

    }

    @Override
    public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        OrderRow full = m_map.get(permId);
        if (full != null) {
            full.getM_state().status(status);
        }
    }

    @Override
    public void handle(int orderId, int errorCode, String errorMsg) {

    }
}

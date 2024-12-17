package com.aixohub.algotrader.broker.ib.model;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;

public class CompletedOrder {

    Contract contract;
    Order order;
    OrderState orderState;

    public CompletedOrder(Contract contract, Order order, OrderState orderState) {
        this.contract = contract;
        this.order = order;
        this.orderState = orderState;
    }

    public CompletedOrder() {
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
package com.aixohub.algotrader.broker.ib.model;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;

public class OrderRow {
    Contract m_contract;
    Order m_order;
    OrderState m_state;

    public OrderRow(Contract contract, Order order, OrderState state) {
        m_contract = contract;
        m_order = order;
        m_state = state;
    }

    public Contract getM_contract() {
        return m_contract;
    }

    public void setM_contract(Contract m_contract) {
        this.m_contract = m_contract;
    }

    public Order getM_order() {
        return m_order;
    }

    public void setM_order(Order m_order) {
        this.m_order = m_order;
    }

    public OrderState getM_state() {
        return m_state;
    }

    public void setM_state(OrderState m_state) {
        this.m_state = m_state;
    }
}

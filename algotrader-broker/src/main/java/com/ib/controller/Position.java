/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.ib.controller;

import com.ib.client.Contract;
import com.ib.client.Decimal;


public class Position {

  private final Contract m_contract;
  private final String m_account;
  private final Decimal m_position;
  private final double m_marketPrice;
  private final double m_marketValue;
  private final double m_averageCost;
  private final double m_unrealPnl;
  private final double m_realPnl;

  public Position(Contract contract, String account, Decimal position, double marketPrice,
      double marketValue, double averageCost, double unrealPnl, double realPnl) {
    m_contract = contract;
    m_account = account;
    m_position = position;
    m_marketPrice = marketPrice;
    m_marketValue = marketValue;
    m_averageCost = averageCost;
    m_unrealPnl = unrealPnl;
    m_realPnl = realPnl;
  }

  public Contract contract() {
    return m_contract;
  }

  public int conid() {
    return m_contract.conid();
  }

  public double averageCost() {
    return m_averageCost;
  }

  public double marketPrice() {
    return m_marketPrice;
  }

  public double marketValue() {
    return m_marketValue;
  }

  public double realPnl() {
    return m_realPnl;
  }

  public double unrealPnl() {
    return m_unrealPnl;
  }

  public Decimal position() {
    return m_position;
  }

  public String account() {
    return m_account;
  }
}

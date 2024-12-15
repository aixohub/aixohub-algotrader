/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.aixohub.algotrader.broker.ib.config;


/**
 * Delegate for connection parameters
 */
public interface IConnectionConfiguration {

  String getDefaultHost();

  int getDefaultPort();

  int getDefaultClientId();

  String getDefaultConnectOptions();

}

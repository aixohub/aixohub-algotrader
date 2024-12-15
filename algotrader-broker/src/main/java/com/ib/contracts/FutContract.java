/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.ib.contracts;

import com.ib.client.Contract;
import com.ib.client.Types;

public class FutContract extends Contract {

  public FutContract(String symbol, String lastTradeDateOrContractMonth) {
    symbol(symbol);
    secType(Types.SecType.FUT);
    exchange("EUREX");
    currency("EUR");
    lastTradeDateOrContractMonth(lastTradeDateOrContractMonth);
  }

  public FutContract(String symbol, String lastTradeDateOrContractMonth, String currency) {
    symbol(symbol);
    secType(Types.SecType.FUT.name());
    currency(currency);
    lastTradeDateOrContractMonth(lastTradeDateOrContractMonth);
  }
}

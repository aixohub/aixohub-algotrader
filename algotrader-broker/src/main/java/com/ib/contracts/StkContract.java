/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.ib.contracts;

import com.ib.client.Contract;
import com.ib.client.Types;

public class StkContract extends Contract {

  public StkContract(String symbol) {
    symbol(symbol);
    secType(Types.SecType.STK.name());
    exchange("SMART");
    currency("USD");
  }
}

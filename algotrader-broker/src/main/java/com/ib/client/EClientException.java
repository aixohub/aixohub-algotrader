/* Copyright (C) 2020 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package com.ib.client;

import java.io.IOException;

class EClientException extends IOException {

  private static final long serialVersionUID = 1L;
  private final EClientErrors.CodeMsgPair m_error;
  private final String m_text;

  EClientException(EClientErrors.CodeMsgPair err, String text) {
    m_error = err;
    m_text = text;
  }

  public EClientErrors.CodeMsgPair error() {
    return m_error;
  }

  public String text() {
    return m_text;
  }
}

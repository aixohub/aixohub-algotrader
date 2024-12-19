package com.aixohub.algotrader.service.quant.observers;

import com.aixohub.algotrader.service.quant.model.MarketDataRow;
import com.ib.controller.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

public interface TickByTickResultObserver extends ApiController.ITickByTickDataHandler {
    Logger logger = LoggerFactory.getLogger(TickByTickResultObserver.class);

    String getSymbol();

    Observable<MarketDataRow> priceObservable();
}

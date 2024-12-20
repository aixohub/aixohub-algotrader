package com.aixohub.algotrader.service.quant.observers;

import com.aixohub.algotrader.service.trading.lib.series.DoubleSeries;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import rx.Observable;

/**
 *
 */
public interface HistoryObserver extends IHistoricalDataHandler {


    Observable<DoubleSeries> observableDoubleSeries();
}

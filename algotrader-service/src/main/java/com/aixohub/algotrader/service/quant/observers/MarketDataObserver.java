package com.aixohub.algotrader.service.quant.observers;

import com.aixohub.algotrader.service.quant.model.MarketDataRow;
import com.ib.client.Decimal;
import com.ib.client.TickType;
import com.ib.controller.ApiController.ITopMktDataHandler;
import rx.Observable;

/**
 *
 */
public interface MarketDataObserver extends ITopMktDataHandler {

    String getSymbol();

    Observable<MarketDataRow> priceObservable();

    @Override
    default void tickSize(TickType tickType, Decimal size) {
    }

    @Override
    default void tickString(TickType tickType, String value) {
    }

    @Override
    default void tickSnapshotEnd() {
    }

    @Override
    default void marketDataType(int marketDataType) {
    }


}

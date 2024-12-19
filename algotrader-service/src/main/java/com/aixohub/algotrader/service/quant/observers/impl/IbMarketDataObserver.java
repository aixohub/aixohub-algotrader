package com.aixohub.algotrader.service.quant.observers.impl;

import com.aixohub.algotrader.service.quant.config.ContractBuilder;
import com.aixohub.algotrader.service.quant.model.MarketDataRow;
import com.aixohub.algotrader.service.quant.observers.MarketDataObserver;
import com.ib.client.TickAttrib;
import com.ib.client.TickType;
import com.ib.controller.ApiController.ITopMktDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Wraps IB {@link ITopMktDataHandler} into observer to simplify
 * access to data (price) feed
 */
public class IbMarketDataObserver implements MarketDataObserver {

    private static final Logger log = LoggerFactory.getLogger(IbMarketDataObserver.class);

    private final String symbol;
    private final PublishSubject<MarketDataRow> priceSubject;

    public IbMarketDataObserver(String symbol) {
        this.symbol = symbol;
        this.priceSubject = PublishSubject.create();
    }

    @Override
    public void tickPrice(TickType tickType, double price, TickAttrib attribs) {
        if (price == -1.0) { // do not update price with bogus value when market is about ot be closed
            return;
        }
        double realPrice = ContractBuilder.getSymbolPrice(symbol, price);

        priceSubject.onNext(new MarketDataRow(tickType, realPrice));
    }

    public String getSymbol() {
        return symbol;
    }

    public Observable<MarketDataRow> priceObservable() {
        return priceSubject.asObservable();
    }


    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {

    }
}

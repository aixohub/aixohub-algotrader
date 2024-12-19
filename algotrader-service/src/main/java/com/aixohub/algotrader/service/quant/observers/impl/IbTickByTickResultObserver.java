package com.aixohub.algotrader.service.quant.observers.impl;

import com.aixohub.algotrader.service.quant.model.MarketDataRow;
import com.aixohub.algotrader.service.quant.observers.TickByTickResultObserver;
import com.ib.client.Decimal;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.client.TickByTick;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.util.List;

public class IbTickByTickResultObserver implements TickByTickResultObserver {

    private final String symbol;
    private final PublishSubject<MarketDataRow> priceSubject;

    public IbTickByTickResultObserver(String symbol, PublishSubject<MarketDataRow> priceSubject) {
        this.symbol = symbol;
        this.priceSubject = priceSubject;
    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size, TickAttribLast tickAttribLast, String exchange, String specialConditions) {

    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize, Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
        TickByTick tick = new TickByTick(time, bidPrice, bidSize, askPrice, askSize, tickAttribBidAsk);

    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {

    }

    @Override
    public void tickByTickHistoricalTickAllLast(int reqId, List<HistoricalTickLast> ticks) {

    }

    @Override
    public void tickByTickHistoricalTickBidAsk(int reqId, List<HistoricalTickBidAsk> ticks) {

    }

    @Override
    public void tickByTickHistoricalTick(int reqId, List<HistoricalTick> ticks) {

    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public Observable<MarketDataRow> priceObservable() {
        return priceSubject.asObservable();
    }
}

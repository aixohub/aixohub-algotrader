package com.aixohub.algotrader.service.quant.observers;

import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.ib.client.Decimal;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 *
 */
public class IbAccountObserver implements AccountObserver {

    private final PublishSubject<Double> cashBalanceSubject;
    private final PublishSubject<Double> netValueSubject;
    private final TradingContext tradingContext;

    public IbAccountObserver(TradingContext tradingContext) {
        cashBalanceSubject = PublishSubject.create();
        netValueSubject = PublishSubject.create();
        this.tradingContext = tradingContext;
    }

    @Override
    public void setCashBalance(double balance) {
        cashBalanceSubject.onNext(balance);
    }

    @Override
    public void setNetValue(double netValue) {
        logger.debug("Setting net value");
        netValueSubject.onNext(netValue);
    }

    @Override
    public void updateSymbolPosition(String symbol, Decimal position) {
        logger.info("{} position: {}", symbol, position);

    }

    public Observable<Double> observableCashBalance() {
        return cashBalanceSubject.asObservable();
    }

    public Observable<Double> observableNetValue() {
        return netValueSubject.asObservable();
    }
}

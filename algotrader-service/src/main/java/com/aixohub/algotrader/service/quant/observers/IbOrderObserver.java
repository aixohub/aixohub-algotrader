package com.aixohub.algotrader.service.quant.observers;

import com.ib.client.Decimal;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 *
 */
public class IbOrderObserver implements OrderObserver {

    private final PublishSubject<OrderState> orderSubject;

    public IbOrderObserver() {
        orderSubject = PublishSubject.create();
    }

    @Override
    public void orderState(OrderState orderState) {
        orderSubject.onNext(orderState);
    }

    @Override
    public void orderStatus(OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {

    }

    public Observable<OrderState> observableOrderState() {
        return orderSubject.asObservable();
    }
}

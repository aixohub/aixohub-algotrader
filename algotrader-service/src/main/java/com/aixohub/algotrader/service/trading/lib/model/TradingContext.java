package com.aixohub.algotrader.service.trading.lib.model;

import com.aixohub.algotrader.service.trading.lib.series.TimeSeries;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

public interface TradingContext {
    Instant getTime();

    double getLastPrice(String instrument);

    Stream<TimeSeries.Entry<Double>> getHistory(String instrument);

    Order order(String instrument, boolean buy, int amount);

    ClosedOrder close(Order order);

    double getPl();

    List<String> getInstruments();

    double getAvailableFunds();

    double getInitialFunds();

    double getNetValue();

    double getLeverage();
}

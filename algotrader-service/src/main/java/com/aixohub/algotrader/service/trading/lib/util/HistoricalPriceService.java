package com.aixohub.algotrader.service.trading.lib.util;

import com.aixohub.algotrader.service.trading.lib.series.DoubleSeries;
import rx.Observable;

public interface HistoricalPriceService {
    Observable<DoubleSeries> getHistoricalAdjustedPrices(String symbol);
}

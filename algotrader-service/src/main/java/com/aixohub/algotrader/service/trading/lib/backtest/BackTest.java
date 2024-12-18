package com.aixohub.algotrader.service.trading.lib.backtest;

import com.aixohub.algotrader.service.quant.strategy.TradingStrategy;
import com.aixohub.algotrader.service.trading.lib.model.ClosedOrder;
import com.aixohub.algotrader.service.trading.lib.util.Statistics;
import com.aixohub.algotrader.service.trading.lib.series.DoubleSeries;
import com.aixohub.algotrader.service.trading.lib.series.MultipleDoubleSeries;
import com.aixohub.algotrader.service.trading.lib.series.TimeSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.aixohub.algotrader.service.trading.lib.util.Util.check;

public class BackTest {

    MultipleDoubleSeries mPriceSeries;
    double mDeposit;
    double mLeverage = 1;
    TradingStrategy mStrategy;
    BackTestTradingContext mContext;
    Iterator<TimeSeries.Entry<List<Double>>> mPriceIterator;
    Result mResult;
    public BackTest(double deposit, MultipleDoubleSeries priceSeries) {
        check(priceSeries.isAscending());
        mDeposit = deposit;
        mPriceSeries = priceSeries;
    }

    public double getLeverage() {
        return mLeverage;
    }

    public void setLeverage(double leverage) {
        mLeverage = leverage;
    }

    public Result run(TradingStrategy strategy) {
        initialize(strategy);
        while (nextStep()) ;
        return mResult;
    }

    public void initialize(TradingStrategy strategy) {
        mStrategy = strategy;
        mContext = (BackTestTradingContext) strategy.getTradingContext();

        mContext.mInstruments = mPriceSeries.getNames();
        mContext.mHistory = new MultipleDoubleSeries(mContext.mInstruments);
        mContext.mInitialFunds = mDeposit;
        mContext.mLeverage = mLeverage;

        mPriceIterator = mPriceSeries.iterator();
        nextStep();
    }

    public boolean nextStep() {
        if (!mPriceIterator.hasNext()) {
            finish();
            return false;
        }

        TimeSeries.Entry<List<Double>> entry = mPriceIterator.next();

        mContext.mPrices = entry.getItem();
        mContext.mInstant = entry.getInstant();
        mContext.mPl.add(mContext.getPl(), entry.getInstant());
        mContext.mFundsHistory.add(mContext.getAvailableFunds(), entry.getInstant());
        if (mContext.getAvailableFunds() < 0) {
            finish();
            return false;
        }

        mStrategy.onTick();

        mContext.mHistory.add(entry);

        return true;
    }

    public Result getResult() {
        return mResult;
    }

    private void finish() {
        for (SimpleOrder order : new ArrayList<>(mContext.mOrders)) {
            mContext.close(order);
        }

        // TODO (replace below code with BackTest results implementation
//        mStrategy.onEnd();

        List<ClosedOrder> orders = Collections.unmodifiableList(mContext.mClosedOrders);
        mResult = new Result(mContext.mClosedPl, mContext.mPl, mContext.mFundsHistory, orders, mDeposit, mDeposit + mContext.mClosedPl, mContext.mCommissions);
    }

    public static class Result {
        DoubleSeries mPlHistory;
        DoubleSeries mMarginHistory;
        double mPl;
        List<ClosedOrder> mOrders;
        double mInitialFund;
        double mFinalValue;
        double mCommissions;

        public Result(double pl, DoubleSeries plHistory, DoubleSeries marginHistory, List<ClosedOrder> orders, double initialFund, double finalValue, double commisions) {
            mPl = pl;
            mPlHistory = plHistory;
            mMarginHistory = marginHistory;
            mOrders = orders;
            mInitialFund = initialFund;
            mFinalValue = finalValue;
            mCommissions = commisions;
        }

        public DoubleSeries getMarginHistory() {
            return mMarginHistory;
        }

        public double getInitialFund() {
            return mInitialFund;
        }

        public DoubleSeries getAccountValueHistory() {
            return getPlHistory().plus(mInitialFund);
        }

        public double getFinalValue() {
            return mFinalValue;
        }

        public double getReturn() {
            return mFinalValue / mInitialFund - 1;
        }

        public double getAnnualizedReturn() {
            return getReturn() * 250 / getDaysCount();
        }

        public double getSharpe() {
            return Statistics.sharpe(Statistics.returns(getAccountValueHistory().toArray()));
        }

        public double getMaxDrawdown() {
            return Statistics.drawdown(getAccountValueHistory().toArray())[0];
        }

        public double getMaxDrawdownPercent() {
            return Statistics.drawdown(getAccountValueHistory().toArray())[1];
        }

        public int getDaysCount() {
            return mPlHistory.size();
        }

        public DoubleSeries getPlHistory() {
            return mPlHistory;
        }

        public double getPl() {
            return mPl;
        }

        public double getCommissions() {
            return mCommissions;
        }

        public List<ClosedOrder> getOrders() {
            return mOrders;
        }
    }
}

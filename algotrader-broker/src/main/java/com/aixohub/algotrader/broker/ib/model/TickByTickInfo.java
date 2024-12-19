package com.aixohub.algotrader.broker.ib.model;

import com.ib.client.Decimal;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.client.Util;

import java.math.BigDecimal;

public class TickByTickInfo {

    private int tickType; // 0 - None, 1 - Last, 2 - AllLast, 3 -BidAsk, 4 - MidPoint
    private String time;  // in seconds
    private double price;
    private BigDecimal size;
    private TickAttribLast tickAttribLast;
    private TickAttribBidAsk tickAttribBidAsk;
    private String exchange;
    private String specialConditions;
    private double bidPrice;
    private BigDecimal bidSize;
    private double askPrice;
    private BigDecimal askSize;
    private double midPoint;

    public TickByTickInfo(long time, double bidPrice, Decimal bidSize, double askPrice, Decimal askSize,
                          TickAttribBidAsk tickAttribBidAsk) {
        this.tickType = 3;
        this.time =  Util.UnixSecondsToString(time, "yyyy-MM-dd HH:mm:ss");
        this.bidPrice = bidPrice;
        this.bidSize = bidSize.toBigDecimal();
        this.askPrice = askPrice;
        this.askSize = askSize.toBigDecimal();
        this.tickAttribBidAsk = tickAttribBidAsk;
    }

    @Override
    public String toString() {
        return "TickByTickInfo{" +
                "tickType=" + tickType +
                ", time=" + time +
                ", price=" + price +
                ", size=" + size +
                ", tickAttribLast=" + tickAttribLast +
                ", tickAttribBidAsk=" + tickAttribBidAsk +
                ", exchange='" + exchange + '\'' +
                ", specialConditions='" + specialConditions + '\'' +
                ", bidPrice=" + bidPrice +
                ", bidSize=" + bidSize +
                ", askPrice=" + askPrice +
                ", askSize=" + askSize +
                ", midPoint=" + midPoint +
                '}';
    }

    public int getTickType() {
        return tickType;
    }

    public void setTickType(int tickType) {
        this.tickType = tickType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public TickAttribLast getTickAttribLast() {
        return tickAttribLast;
    }

    public void setTickAttribLast(TickAttribLast tickAttribLast) {
        this.tickAttribLast = tickAttribLast;
    }

    public TickAttribBidAsk getTickAttribBidAsk() {
        return tickAttribBidAsk;
    }

    public void setTickAttribBidAsk(TickAttribBidAsk tickAttribBidAsk) {
        this.tickAttribBidAsk = tickAttribBidAsk;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSpecialConditions() {
        return specialConditions;
    }

    public void setSpecialConditions(String specialConditions) {
        this.specialConditions = specialConditions;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getBidSize() {
        return bidSize;
    }

    public void setBidSize(BigDecimal bidSize) {
        this.bidSize = bidSize;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getAskSize() {
        return askSize;
    }

    public void setAskSize(BigDecimal askSize) {
        this.askSize = askSize;
    }

    public double getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(double midPoint) {
        this.midPoint = midPoint;
    }
}

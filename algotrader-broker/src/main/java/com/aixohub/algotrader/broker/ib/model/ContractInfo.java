package com.aixohub.algotrader.broker.ib.model;

import com.ib.client.DeltaNeutralContract;

public class ContractInfo {

    private int conid;
    private String symbol;
    private String secType;
    private String lastTradeDateOrContractMonth;
    private double strike;
    private String right;
    private String multiplier; // should be double
    private String exchange;
    private String primaryExch; // pick a non-aggregate (ie not the SMART exchange) exchange that the contract trades on.  DO NOT SET TO SMART.
    private String currency;
    private String localSymbol;
    private String tradingClass;
    private String secIdType; // CUSIP;SEDOL;ISIN;RIC
    private String secId;
    private String description;
    private String issuerId;

    private DeltaNeutralContract deltaNeutralContract;
    private boolean includeExpired;  // can not be set to true for orders

    public int getConid() {
        return conid;
    }

    public void setConid(int conid) {
        this.conid = conid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecType() {
        return secType;
    }

    public void setSecType(String secType) {
        this.secType = secType;
    }

    public String getLastTradeDateOrContractMonth() {
        return lastTradeDateOrContractMonth;
    }

    public void setLastTradeDateOrContractMonth(String lastTradeDateOrContractMonth) {
        this.lastTradeDateOrContractMonth = lastTradeDateOrContractMonth;
    }

    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(String multiplier) {
        this.multiplier = multiplier;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getPrimaryExch() {
        return primaryExch;
    }

    public void setPrimaryExch(String primaryExch) {
        this.primaryExch = primaryExch;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocalSymbol() {
        return localSymbol;
    }

    public void setLocalSymbol(String localSymbol) {
        this.localSymbol = localSymbol;
    }

    public String getTradingClass() {
        return tradingClass;
    }

    public void setTradingClass(String tradingClass) {
        this.tradingClass = tradingClass;
    }

    public String getSecIdType() {
        return secIdType;
    }

    public void setSecIdType(String secIdType) {
        this.secIdType = secIdType;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public DeltaNeutralContract getDeltaNeutralContract() {
        return deltaNeutralContract;
    }

    public void setDeltaNeutralContract(DeltaNeutralContract deltaNeutralContract) {
        this.deltaNeutralContract = deltaNeutralContract;
    }

    public boolean isIncludeExpired() {
        return includeExpired;
    }

    public void setIncludeExpired(boolean includeExpired) {
        this.includeExpired = includeExpired;
    }

    @Override
    public String toString() {
        return "ContractInfo{" +
                "conid=" + conid +
                ", symbol='" + symbol + '\'' +
                ", secType='" + secType + '\'' +
                ", lastTradeDateOrContractMonth='" + lastTradeDateOrContractMonth + '\'' +
                ", strike=" + strike +
                ", right='" + right + '\'' +
                ", multiplier='" + multiplier + '\'' +
                ", exchange='" + exchange + '\'' +
                ", primaryExch='" + primaryExch + '\'' +
                ", currency='" + currency + '\'' +
                ", localSymbol='" + localSymbol + '\'' +
                ", tradingClass='" + tradingClass + '\'' +
                ", secIdType='" + secIdType + '\'' +
                ", secId='" + secId + '\'' +
                ", description='" + description + '\'' +
                ", issuerId='" + issuerId + '\'' +
                ", deltaNeutralContract=" + deltaNeutralContract +
                ", includeExpired=" + includeExpired +
                '}';
    }
}

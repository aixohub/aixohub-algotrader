package com.aixohub.algotrader.broker.ib.model;


import com.ib.client.Decimal;

public class PositionInfo {
    String account;
    ContractInfo contract;
    Decimal pos;
    Double avgCost;

    public PositionInfo(String account, ContractInfo contract, Decimal pos, Double avgCost) {
        this.account = account;
        this.contract = contract;
        this.pos = pos;
        this.avgCost = avgCost;
    }

    public PositionInfo() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ContractInfo getContract() {
        return contract;
    }

    public void setContract(ContractInfo contract) {
        this.contract = contract;
    }

    public Decimal getPos() {
        return pos;
    }

    public void setPos(Decimal pos) {
        this.pos = pos;
    }


    public Double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(Double avgCost) {
        this.avgCost = avgCost;
    }

    @Override
    public String toString() {
        return "PositionInfo{" +
                "account='" + account + '\'' +
                ", contract=" + contract +
                ", pos=" + pos +
                ", avgCost=" + avgCost +
                '}';
    }
}



package com.aixohub.algotrader.base.model;

public class AccountInfo {
    private String accountName;
    private String key;
    private String currency;
    private String value;

    public AccountInfo(String accountName, String key, String currency, String value) {
        this.accountName = accountName;
        this.key = key;
        this.currency = currency;
        this.value = value;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "accountName='" + accountName + '\'' +
                ", key='" + key + '\'' +
                ", currency='" + currency + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

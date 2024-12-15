package com.aixohub.algotrader.broker.ib.config;

public class DefaultConnectionConfiguration implements IConnectionConfiguration {

    @Override
    public String getDefaultHost() {
        return "127.0.0.1";
    }

    @Override
    public int getDefaultPort() {
        return 4001;
    }

    @Override
    public int getDefaultClientId() {
        return 8;
    }

    @Override
    public String getDefaultConnectOptions() {
        return null;
    }
}
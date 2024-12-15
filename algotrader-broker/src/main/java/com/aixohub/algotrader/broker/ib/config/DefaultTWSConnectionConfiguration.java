package com.aixohub.algotrader.broker.ib.config;

public class DefaultTWSConnectionConfiguration implements IConnectionConfiguration {
    @Override
    public String getDefaultHost() {
        return "127.0.0.1";
    }

    @Override
    public int getDefaultPort() {
        return 9796;
    }

    @Override
    public int getDefaultClientId() {
        return 3;
    }

    @Override
    public String getDefaultConnectOptions() {
        return null;
    }
}

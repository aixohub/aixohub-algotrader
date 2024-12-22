package com.aixohub.algotrader.service.calc;

public class TaxiRide {
    private Boolean nightSurchargeFlag;
    private Long distanceInMile;

    // standard constructors, getters/setters


    public Boolean getNightSurchargeFlag() {
        return nightSurchargeFlag;
    }

    public void setNightSurchargeFlag(Boolean nightSurchargeFlag) {
        this.nightSurchargeFlag = nightSurchargeFlag;
    }

    public Long getDistanceInMile() {
        return distanceInMile;
    }

    public void setDistanceInMile(Long distanceInMile) {
        this.distanceInMile = distanceInMile;
    }
}

package com.aixohub.algotrader.service.calc;

public class Fare {
    private Long nightSurcharge;
    private Long rideFare;

    private Long totalFare;

    // standard constructors, getters/setters


    public Long getTotalFare() {
        return nightSurcharge + rideFare;
    }

    public Long getNightSurcharge() {
        return nightSurcharge;
    }

    public void setNightSurcharge(Long nightSurcharge) {
        this.nightSurcharge = nightSurcharge;
    }

    public Long getRideFare() {
        return rideFare;
    }

    public void setRideFare(Long rideFare) {
        this.rideFare = rideFare;
    }
}
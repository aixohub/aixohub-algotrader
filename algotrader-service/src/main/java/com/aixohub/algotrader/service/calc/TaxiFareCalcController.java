package com.aixohub.algotrader.service.calc;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaxiFareCalcController {

    @Autowired
    private TaxiFareCalculatorService taxiFareCalculatorService;

    @RequestMapping("/test")
    @ResponseBody
    public Long send(){
        TaxiRide taxiRide = new TaxiRide();
        taxiRide.setNightSurchargeFlag(false);
        taxiRide.setDistanceInMile(9L);
        Fare rideFare = new Fare();
        Long totalCharge = taxiFareCalculatorService.calculateFare(taxiRide, rideFare);
        return totalCharge;
    }


}


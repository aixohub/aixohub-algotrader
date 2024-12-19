package com.aixohub.algotrader.broker.ib;

import com.aixohub.algotrader.base.model.AccountInfo;
import com.aixohub.algotrader.base.utils.JsonUtils;
import com.aixohub.algotrader.broker.ib.config.DefaultConnectionConfiguration;
import com.aixohub.algotrader.broker.ib.model.PositionInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class IbApiDemo {

    public static void main(String[] args) {
        IBBroker ibBroker = new IBBroker();
        ibBroker.start(new DefaultConnectionConfiguration());

        BigDecimal cash = ibBroker.getCash("");
        Map<String, List<AccountInfo>> accMap = ibBroker.getAccMap();
        PositionInfo position = ibBroker.getPosition("");
        ibBroker.reqTickByTickData("MSTR");
        ibBroker.reqTickByTickData("AVGO");
        System.out.println("position= "+ JsonUtils.toJson(position));
    }
}

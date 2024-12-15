package com.aixohub.algotrader.broker.ib.handler;

import com.aixohub.algotrader.base.model.AccountInfo;
import com.aixohub.algotrader.broker.ib.IBStore;
import com.ib.controller.ApiController;
import com.ib.controller.Position;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


public class DefaultAccountHandler implements ApiController.IAccountHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAccountHandler.class);

    IBStore ibStore;

    Map<String, List<AccountInfo>> accMap = new ConcurrentHashMap<>();

    Map<String, BigDecimal> accValueMap = new ConcurrentHashMap<>();

    Map<String, BigDecimal> accCashMap = new ConcurrentHashMap<>();

    public DefaultAccountHandler(IBStore ibStore) {
        this.ibStore = ibStore;
    }

    @Override
    public void accountValue(String account, String key, String value, String currency) {
        AccountInfo accountInfo = new AccountInfo(account, key, value, currency);

        List<AccountInfo> accountInfoList = accMap.get(account);
        if (CollectionUtils.isEmpty(accountInfoList)) {
            accountInfoList = new ArrayList<>();
            accountInfoList.add(accountInfo);
        } else {
            accountInfoList.add(accountInfo);
        }


        accMap.put(account, accountInfoList);
        if (StringUtils.equals(key, "NetLiquidation")) {
            BigDecimal val = BigDecimal.ZERO;
            if (StringUtils.isNotBlank(value)) {
                val = new BigDecimal(value);
            }
            accValueMap.put(account, val);
        } else if (StringUtils.equals(key, "CashBalance") && StringUtils.equals(currency, "BASE")) {
            BigDecimal val = BigDecimal.ZERO;
            if (StringUtils.isNotBlank(value)) {
                val = new BigDecimal(value);
            }
            accCashMap.put(account, val);
        }
        CountDownLatch latch = ibStore.getLatch();
        logger.info("DefaultAccountHandler-account:{} key: {} value: {} currency: {}", account, key, value, currency);
    }

    @Override
    public void accountTime(String timeStamp) {

    }

    @Override
    public void accountDownloadEnd(String account) {
        ibStore.getLatch().countDown();
        CountDownLatch latch = ibStore.getLatch();
        logger.info("DefaultAccountHandler-accountDownloadEnd-account:{} ", account);

        ibStore.setAccountCashMap(accCashMap);
        ibStore.setAccountValueMap(accValueMap);
        ibStore.setAccMap(accMap);

    }

    @Override
    public void updatePortfolio(Position position) {

    }
}

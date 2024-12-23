package com.aixohub.algotrader.service.quant.observers;

import com.ib.client.Decimal;
import com.ib.controller.ApiController.IAccountHandler;
import com.ib.controller.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public interface AccountObserver extends IAccountHandler {

    Logger logger = LoggerFactory.getLogger(AccountObserver.class);

    default void accountValue(String account, String key, String value, String currency) {
        if (key.equals("NetLiquidation") && currency.equals("USD")) {

            logger.info("account: {}, key: {}, value: {}, currency: {}",
                    account, key, value, currency);
            setNetValue(Double.parseDouble(value));
        }
        if (key.equals("AvailableFunds") && currency.equals("USD")) {
            logger.info("account: {}, key: {}, value: {}, currency: {}",
                    account, key, value, currency);
            setCashBalance(Double.parseDouble(value));
        }
    }

    default void accountTime(String timeStamp) {
        logger.info(String.format("account time: %s", timeStamp));
    }

    default void accountDownloadEnd(String account) {
        logger.info("accountDownloadEnd account: {}", account);
    }

    @Override
    default void updatePortfolio(Position position) {
        updateSymbolPosition(position.contract().symbol(), position.position());
    }

    void setCashBalance(double balance);

    void setNetValue(double netValue);

    void updateSymbolPosition(String symbol, Decimal position);

}

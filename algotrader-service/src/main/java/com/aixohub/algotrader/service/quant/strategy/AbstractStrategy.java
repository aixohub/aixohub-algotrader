package com.aixohub.algotrader.service.quant.strategy;

import com.aixohub.algotrader.service.quant.context.TradingContext;
import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Abstract strategy class.
 */
public abstract class AbstractStrategy implements TradingStrategy {

    protected final TradingContext tradingContext;
    protected List<Criterion> commonCriteria = Lists.newCopyOnWriteArrayList();
    protected List<Criterion> entryCriteria = Lists.newCopyOnWriteArrayList();
    protected List<Criterion> exitCriteria = Lists.newCopyOnWriteArrayList();
    protected List<Criterion> stopLossCriteria = Lists.newCopyOnWriteArrayList();
    protected List<String> symbols = Lists.newLinkedList();

    public AbstractStrategy(TradingContext tradingContext) {
        this.tradingContext = tradingContext;
    }

    @Override
    public void addEntryCriterion(Criterion criterion) {
        criterion.init();
        entryCriteria.add(criterion);
    }

    @Override
    public void removeEntryCriterion(Criterion criterion) {
        entryCriteria.remove(criterion);
    }

    @Override
    public void addCommonCriterion(Criterion criterion) {
        criterion.init();
        commonCriteria.add(criterion);
    }

    @Override
    public void removeCommonCriterion(Criterion criterion) {
        commonCriteria.remove(criterion);
    }


    @Override
    public void addExitCriterion(Criterion criterion) {
        criterion.init();
        exitCriteria.add(criterion);
    }

    @Override
    public void removeExitCriterion(Criterion criterion) {
        exitCriteria.remove(criterion);
    }

    @Override
    public boolean isCommonCriteriaMet() {
        return testCriteria(commonCriteria);
    }

    @Override
    public boolean isEntryCriteriaMet() {
        return testCriteria(entryCriteria);
    }

    @Override
    public boolean isExitCriteriaMet() {
        return !exitCriteria.isEmpty() && testCriteria(exitCriteria);
    }

    @Override
    public boolean isStopLossCriteriaMet() {
        return !stopLossCriteria.isEmpty() && testCriteria(stopLossCriteria);
    }

    @Override
    public void addStopLossCriterion(Criterion criterion) {
        checkArgument(criterion != null, "criterion is null");

        stopLossCriteria.add(criterion);
    }

    @Override
    public BackTestResult getBackTestResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addSymbol(String symbol) {
        symbols.add(symbol);
        tradingContext.addContract(symbol);
    }

    @Override
    public TradingContext getTradingContext() {
        return tradingContext;
    }

    private boolean testCriteria(List<Criterion> criteria) {
        if (criteria.isEmpty()) {
            return true;
        }

        for (Criterion criterion : criteria) {
            try {
                if (!criterion.isMet()) {
                    log.debug("{} criterion was NOT met", criterion.getClass().getName());
                    return false;
                }
                log.debug("{} criterion was met", criterion.getClass().getName());
            } catch (CriterionViolationException e) {
                log.debug("{} criterion was NOT met", criterion.getClass().getName());
                return false;
            }
        }
        return true;
    }
}

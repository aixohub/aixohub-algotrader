package com.aixohub.algotrader.service.quant.strategy;


import com.aixohub.algotrader.service.quant.exception.CriterionViolationException;

/**
 * Criterion interface.
 */
public interface Criterion {

    /**
     * To be run if criterion needs tobe initialized .E.g. to get the historical or other data needed
     * for calculation.
     */
    default void init() {
    }

    /**
     * Check if criterion has been met
     *
     * @return true if criteria is met, false otherwise
     */
    boolean isMet() throws CriterionViolationException;
}

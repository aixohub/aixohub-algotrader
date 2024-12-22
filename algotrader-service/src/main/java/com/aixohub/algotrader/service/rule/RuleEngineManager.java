package com.aixohub.algotrader.service.rule;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

// 规则引擎管理器
public class RuleEngineManager {
    private KieContainer kieContainer;

    public RuleEngineManager(String rulesFilePath) {
        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write(ResourceFactory.newClassPathResource(rulesFilePath));
        KieBuilder kb = ks.newKieBuilder(kfs);
        kb.buildAll();
        this.kieContainer = ks.newKieContainer(kb.getKieModule().getReleaseId());
    }

    public void evaluateRules(MarketCondition condition, StrategyManager strategyManager) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(condition);
        kieSession.setGlobal("strategyManager", strategyManager);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}

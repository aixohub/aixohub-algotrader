package com.aixohub.algotrader.service.rule;

import org.kie.api.KieServices;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class TradingStrategyEngine {
    public static void main(String[] args) {
        try {
            // 初始化 Drools
            KieServices kieServices = KieServices.Factory.get();


            KieRepository kieRepository = kieServices.getRepository();

            ReleaseId krDefaultReleaseId = kieRepository.getDefaultReleaseId();
            KieContainer kieContainer
                    = kieServices.newKieContainer(krDefaultReleaseId);

            KieSession kSession = kieContainer.newKieSession("ksession-rules");

            // 创建策略上下文
            TradingStrategyContext context = new TradingStrategyContext();

            // 设置全局变量
            kSession.setGlobal("strategyContext", context);

            // 模拟市场数据
            double volatility = 0.06; // 假设波动率为 6%
            kSession.insert(volatility);

            // 执行规则
            kSession.fireAllRules();

            // 执行当前策略
            context.executeStrategy();

            kSession.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
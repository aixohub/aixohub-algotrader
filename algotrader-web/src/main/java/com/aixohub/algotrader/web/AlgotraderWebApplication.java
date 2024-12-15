package com.aixohub.algotrader.web;

import com.aixohub.algotrader.base.utils.BuildInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class AlgotraderWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgotraderWebApplication.class, args);
		LocalDateTime buildTime =
				BuildInfo.INSTANCE.getBuildTime();
		System.out.println(buildTime);
	}

}

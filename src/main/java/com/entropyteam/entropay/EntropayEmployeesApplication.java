package com.entropyteam.entropay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class EntropayEmployeesApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EntropayEmployeesApplication.class);
		app.setApplicationStartup(new BufferingApplicationStartup(10000));
		app.run(args);
	}

}

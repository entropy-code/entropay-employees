package com.entropyteam.entropay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EntropayEmployeesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntropayEmployeesApplication.class, args);
	}

}

package com.cct.onlineteaching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnlineTeachingApplication {
	public static void main(String[] args) {
		SpringApplication.run(OnlineTeachingApplication.class, args);
	}

}


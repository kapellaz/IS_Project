package com.example.isproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@EnableR2dbcRepositories
@SpringBootApplication
public class IsprojectApplication {
	static final Logger logger = LoggerFactory.getLogger(IsprojectApplication.class);

	public static void main(String[] args) {
		logger.info("Starting application");
		SpringApplication.run(IsprojectApplication.class, args);
	}

}

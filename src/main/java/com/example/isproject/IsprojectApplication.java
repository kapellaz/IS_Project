package com.example.isproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import java.util.logging.Logger;

// Enable R2DBC repositories
@EnableR2dbcRepositories
// Enable Spring Boot application
@SpringBootApplication
public class IsprojectApplication {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(IsprojectApplication.class.getName());

		SpringApplication.run(IsprojectApplication.class, args);
	}

}

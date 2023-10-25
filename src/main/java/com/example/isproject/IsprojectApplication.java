package com.example.isproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;



@EnableR2dbcRepositories
@SpringBootApplication
public class IsprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsprojectApplication.class, args);
	}

}

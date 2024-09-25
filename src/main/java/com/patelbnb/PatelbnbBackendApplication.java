package com.patelbnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = "com.patelbnb.user.mapper")
public class PatelbnbBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatelbnbBackendApplication.class, args);
	}
}

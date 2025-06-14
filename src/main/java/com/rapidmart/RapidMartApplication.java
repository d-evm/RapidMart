package com.rapidmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RapidMartApplication {

	public static void main(String[] args) {
		SpringApplication.run(RapidMartApplication.class, args);
	}

}

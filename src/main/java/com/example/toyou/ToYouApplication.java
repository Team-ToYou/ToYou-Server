package com.example.toyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ToYouApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToYouApplication.class, args);
	}
}

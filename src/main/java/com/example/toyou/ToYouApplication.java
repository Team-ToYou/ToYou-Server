package com.example.toyou;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@OpenAPIDefinition(
		servers = {
				@Server(url = "https://to-you.store", description = "Production Server"),
				@Server(url = "http://localhost:8080", description = "Local Development Server")
		}
)
public class ToYouApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		SpringApplication.run(ToYouApplication.class, args);
	}
}

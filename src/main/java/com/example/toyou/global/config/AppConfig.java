package com.example.toyou.global.config;

import com.vane.badwordfiltering.BadWordFiltering;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public BadWordFiltering badWordFiltering() {
        return new BadWordFiltering();
    }
}

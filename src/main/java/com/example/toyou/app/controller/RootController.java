package com.example.toyou.app.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class RootController {

    @GetMapping("/health")
    public ResponseEntity<String> HealthCheck() {
        return ResponseEntity.ok("I'm Healthy");
    }
}

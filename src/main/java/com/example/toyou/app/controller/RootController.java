package com.example.toyou.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/health")
    public ResponseEntity<String> HealthCheck() {
        return ResponseEntity.ok("I'm Healthy");
    }
}

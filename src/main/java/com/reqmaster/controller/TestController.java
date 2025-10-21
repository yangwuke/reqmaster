package com.reqmaster.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        return Map.of(
                "status", "OK",
                "message", "应用运行正常",
                "timestamp", java.time.LocalDateTime.now().toString()
        );
    }

    @GetMapping("/env")
    public Map<String, String> getEnvironment() {
        return Map.of(
                "java.version", System.getProperty("java.version"),
                "user.dir", System.getProperty("user.dir"),
                "os.name", System.getProperty("os.name")
        );
    }
}
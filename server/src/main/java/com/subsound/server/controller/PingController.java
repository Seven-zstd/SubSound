package com.subsound.server.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest")
public class PingController {
    @GetMapping("/ping.view")
    public Map<String, Object> ping() {
        return Map.of(
                "subsonic-response", Map.of(
                        "status", "ok",
                        "version", "1.16.1",
                        "type", "SubSound",
                        "serverVersion", "0.0.1"
                )
        );
    }
}
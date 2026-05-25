package com.neusoft.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping("/fallback")
    public Map<String, Object> fallback() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("message", "Service temporarily unavailable, please retry later");
        return result;
    }
}

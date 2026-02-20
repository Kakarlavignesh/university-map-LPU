package com.university.map.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "Backend is running. Please open <a href='http://localhost:5174'>http://localhost:5174</a> to see the Map UI.";
    }
}

package com.example.uangku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        // forward to static index.html on the classpath
        return "forward:/index.html";
    }
}

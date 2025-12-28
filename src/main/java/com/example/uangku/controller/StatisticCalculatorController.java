package com.example.uangku.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uangku.service.StatisticCalculatorService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticCalculatorController {
    private final StatisticCalculatorService statisticCalculatorService;

    @GetMapping("/balance")
    public double balance(HttpSession session) {
        com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
        if (user == null)
            return 0.0;
        return statisticCalculatorService.getBalance(user);
    }
}

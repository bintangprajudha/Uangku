package com.example.uangku.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uangku.service.StatisticCalculatorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticCalculatorController {
    private final StatisticCalculatorService statisticCalculatorService;

    @GetMapping("/balance")
    public double balance() {
        return statisticCalculatorService.getBalance();
    }
}

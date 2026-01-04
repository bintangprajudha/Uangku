package com.example.uangku.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.uangku.service.ReportGenerator;
import com.example.uangku.service.StatisticCalculatorService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportGenerator reportGenerator;
    private final StatisticCalculatorService statisticCalculatorService;

    @GetMapping("/reports")
    public String reports(Model model, HttpSession session) {
        com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
        if (user == null)
            return "redirect:/auth/login";

        // Generate current month report
        com.example.uangku.model.Report currentReport = reportGenerator.generateMonthlyReport(user, java.time.LocalDate.now().getYear(), java.time.LocalDate.now().getMonthValue());
        model.addAttribute("report", currentReport);

        // Get monthly summaries for the last 12 months for income vs expense chart
        List<Map<String, Object>> monthlySummaries = statisticCalculatorService.getMonthlySummaries(user);
        model.addAttribute("monthlySummaries", monthlySummaries);

        return "reports";
    }
}

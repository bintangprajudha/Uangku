package com.example.uangku.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuickStatsDTO {
    private Double todayExpense;
    private Double dailyAverage;
    private String topCategory;
    private Double topCategoryAmount;
    private Integer monthProgress;
    private Integer currentDay;
    private Integer totalDaysInMonth;
    private Integer savingsRate;
    private Double totalIncome;
    private Double totalExpense;
    private Double totalSavings;
    private Boolean isTodayAboveAverage;
    private String savingsRateStatus;
}
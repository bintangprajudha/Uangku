package com.example.uangku.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatsDTO {
    private String categoryName;
    private Double totalAmount;
    private Integer transactionCount;
    private Double percentage;
}
package com.example.uangku.dto;

import java.time.LocalDate;

import com.example.uangku.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SearchCriteriaDTO {
    private User user;
    private String keyword;
    private String type;
    private Long categoryId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double minAmount;
    private Double maxAmount;
}
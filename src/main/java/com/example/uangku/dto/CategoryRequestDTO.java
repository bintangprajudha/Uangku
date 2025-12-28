package com.example.uangku.dto;

import lombok.Data;

@Data
public class CategoryRequestDTO {
    private String name;
    private String type;   // income / expense
    private String icon;
    private String color;
}

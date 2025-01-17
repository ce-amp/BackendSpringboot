package com.example.quiz.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;
}
package com.example.quiz.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "Username is required")
    private String username;
}
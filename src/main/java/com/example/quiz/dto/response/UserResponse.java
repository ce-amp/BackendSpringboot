package com.example.quiz.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String role;
    private Integer points;
    private List<UserResponse> following;
    private List<UserResponse> followers;
}
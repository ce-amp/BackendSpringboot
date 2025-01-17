package com.example.quiz.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LeaderboardResponse {
    private String username;
    private Integer points;
    private Integer rank;
}
package com.example.quiz.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AnswerResponse {
    private boolean correct;
    private int pointsEarned;
    private String feedback;
}
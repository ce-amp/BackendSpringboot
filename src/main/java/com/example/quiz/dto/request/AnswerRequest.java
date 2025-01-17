package com.example.quiz.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class AnswerRequest {
    @NotBlank(message = "Answer is required")
    private String answer;
}
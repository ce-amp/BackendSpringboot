package com.example.quiz.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionRequest {
    @NotBlank(message = "Question text is required")
    private String text;

    @NotNull(message = "Options are required")
    @Size(min = 2, message = "At least 2 options are required")
    private List<String> options;

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Difficulty is required")
    private Integer difficulty;

    private List<Long> relatedQuestions;
}
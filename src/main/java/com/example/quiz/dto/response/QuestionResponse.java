package com.example.quiz.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class QuestionResponse {
    private Long id;
    private String text;
    private List<String> options;
    private CategoryResponse category;
    private Integer difficulty;
    private Long creatorId;
    private List<Long> relatedQuestionIds;
    // Correct answer is only included for designers
    private String correctAnswer;
}
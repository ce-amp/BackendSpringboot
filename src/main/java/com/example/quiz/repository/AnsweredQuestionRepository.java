package com.example.quiz.repository;

import com.example.quiz.entity.AnsweredQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnsweredQuestionRepository extends JpaRepository<AnsweredQuestion, Long> {
    List<AnsweredQuestion> findByUserId(Long userId);
    boolean existsByUserIdAndQuestionId(Long userId, Long questionId);
}
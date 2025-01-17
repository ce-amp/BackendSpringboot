package com.example.quiz.repository;

import com.example.quiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Collection;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCreatorId(Long creatorId);
    List<Question> findByCategoryId(Long categoryId);
    List<Question> findByDifficulty(Integer difficulty);
    List<Question> findByIdNotIn(Collection<Long> ids, Pageable pageable);
}
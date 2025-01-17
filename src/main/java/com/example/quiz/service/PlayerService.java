package com.example.quiz.service;

import com.example.quiz.dto.request.AnswerRequest;
import com.example.quiz.dto.response.*;
import com.example.quiz.entity.*;
import com.example.quiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Service
public class PlayerService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AnsweredQuestionRepository answeredQuestionRepository;

    @Autowired
    private UserService userService;

    private int calculatePoints(int difficulty) {
        return 10 * difficulty;
    }

    public List<QuestionResponse> getQuestions(String categoryName, Integer difficulty) {
        User currentUser = userService.getCurrentUser();
        List<Question> questions;

        if (categoryName != null) {
            Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
            questions = questionRepository.findByCategoryId(category.getId());
        } else if (difficulty != null) {
            questions = questionRepository.findByDifficulty(difficulty);
        } else {
            questions = questionRepository.findAll();
        }

        Set<Long> answeredIds = currentUser.getAnsweredQuestions().stream()
            .map(aq -> aq.getQuestion().getId())
            .collect(Collectors.toSet());

        return questions.stream()
            .filter(q -> !answeredIds.contains(q.getId()))
            .map(this::mapToQuestionResponse)
            .collect(Collectors.toList());
    }

    public List<QuestionResponse> getRandomQuestions() {
        User currentUser = userService.getCurrentUser();
        Set<Long> answeredIds = currentUser.getAnsweredQuestions().stream()
            .map(aq -> aq.getQuestion().getId())
            .collect(Collectors.toSet());

        List<Question> allQuestions = questionRepository.findAll();
        List<Question> unansweredQuestions = allQuestions.stream()
            .filter(q -> !answeredIds.contains(q.getId()))
            .collect(Collectors.toList());

        Collections.shuffle(unansweredQuestions);
        return unansweredQuestions.stream()
            .limit(10)
            .map(this::mapToQuestionResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public AnswerResponse submitAnswer(Long questionId, AnswerRequest request) {
        User currentUser = userService.getCurrentUser();
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new RuntimeException("Question not found"));

        if (answeredQuestionRepository.existsByUserIdAndQuestionId(currentUser.getId(), questionId)) {
            throw new RuntimeException("Question already answered");
        }

        boolean isCorrect = question.getCorrectAnswer().equals(request.getAnswer());
        int pointsEarned = isCorrect ? calculatePoints(question.getDifficulty()) : 0;

        AnsweredQuestion answeredQuestion = new AnsweredQuestion();
        answeredQuestion.setUser(currentUser);
        answeredQuestion.setQuestion(question);
        answeredQuestion.setWasCorrect(isCorrect);
        answeredQuestionRepository.save(answeredQuestion);

        currentUser.setPoints(currentUser.getPoints() + pointsEarned);
        userRepository.save(currentUser);

        return new AnswerResponse(isCorrect, pointsEarned, 
            isCorrect ? "Correct answer!" : "Wrong answer");
    }

    public List<LeaderboardResponse> getLeaderboard() {
        return userRepository.findTop10ByRoleOrderByPointsDesc(Role.PLAYER).stream()
            .map(user -> new LeaderboardResponse(
                user.getUsername(),
                user.getPoints(),
                0 // Rank will be calculated in the controller
            ))
            .collect(Collectors.toList());
    }

    private QuestionResponse mapToQuestionResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setText(question.getText());
        response.setOptions(question.getOptions());
        response.setCategory(new CategoryResponse(
            question.getCategory().getId(),
            question.getCategory().getName(),
            question.getCategory().getCreator().getId()
        ));
        response.setDifficulty(question.getDifficulty());
        return response;
    }
}
package com.example.quiz.service;

import com.example.quiz.dto.request.CategoryRequest;
import com.example.quiz.dto.request.QuestionRequest;
import com.example.quiz.dto.response.CategoryResponse;
import com.example.quiz.dto.response.QuestionResponse;
import com.example.quiz.entity.Category;
import com.example.quiz.entity.Question;
import com.example.quiz.entity.User;
import com.example.quiz.repository.CategoryRepository;
import com.example.quiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignerService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    // Question methods
    public List<QuestionResponse> getQuestions(String categoryName, Integer difficulty) {
        User currentUser = userService.getCurrentUser();
        List<Question> questions;

        if (categoryName != null && difficulty != null) {
            Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
            questions = questionRepository.findByCategoryId(category.getId())
                .stream()
                .filter(q -> q.getDifficulty().equals(difficulty))
                .collect(Collectors.toList());
        } else if (categoryName != null) {
            Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
            questions = questionRepository.findByCategoryId(category.getId());
        } else if (difficulty != null) {
            questions = questionRepository.findByDifficulty(difficulty);
        } else {
            questions = questionRepository.findByCreatorId(currentUser.getId());
        }

        return questions.stream()
            .map(this::mapToQuestionResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request) {
        User currentUser = userService.getCurrentUser();
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));

        Question question = new Question();
        question.setText(request.getText());
        question.setOptions(request.getOptions());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setCategory(category);
        question.setDifficulty(request.getDifficulty());
        question.setCreator(currentUser);

        question = questionRepository.save(question);
        return mapToQuestionResponse(question);
    }

    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        User currentUser = userService.getCurrentUser();
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to update this question");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));

        question.setText(request.getText());
        question.setOptions(request.getOptions());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setCategory(category);
        question.setDifficulty(request.getDifficulty());

        question = questionRepository.save(question);
        return mapToQuestionResponse(question);
    }

    // Category methods
    public List<CategoryResponse> getCategories() {
        User currentUser = userService.getCurrentUser();
        return categoryRepository.findByCreatorId(currentUser.getId())
            .stream()
            .map(this::mapToCategoryResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        User currentUser = userService.getCurrentUser();
        Category category = new Category();
        category.setName(request.getName());
        category.setCreator(currentUser);
        
        category = categoryRepository.save(category);
        return mapToCategoryResponse(category);
    }

    public void deleteCategory(Long id) {
        User currentUser = userService.getCurrentUser();
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to delete this category");
        }

        categoryRepository.delete(category);
    }

    // Mapping methods
    private QuestionResponse mapToQuestionResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setText(question.getText());
        response.setOptions(question.getOptions());
        response.setCorrectAnswer(question.getCorrectAnswer());
        response.setCategory(mapToCategoryResponse(question.getCategory()));
        response.setDifficulty(question.getDifficulty());
        response.setCreatorId(question.getCreator().getId());
        return response;
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setCreatorId(category.getCreator().getId());
        return response;
    }
}

package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.quiz.service.DesignerService;
import com.example.quiz.dto.request.QuestionRequest;
import com.example.quiz.dto.request.CategoryRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/designer")
@PreAuthorize("hasRole('DESIGNER')")
public class DesignerController {

    @Autowired
    private DesignerService designerService;

    // Questions endpoints
    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer difficulty) {
        try {
            return ResponseEntity.ok(designerService.getQuestions(category, difficulty));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/questions")
    public ResponseEntity<?> createQuestion(@RequestBody @Valid QuestionRequest request) {
        try {
            return ResponseEntity.ok(designerService.createQuestion(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(designerService.getQuestion(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long id,
            @RequestBody @Valid QuestionRequest request) {
        try {
            return ResponseEntity.ok(designerService.updateQuestion(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            designerService.deleteQuestion(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/questions/{id}/related/{relatedId}")
    public ResponseEntity<?> addRelatedQuestion(
            @PathVariable Long id,
            @PathVariable Long relatedId) {
        try {
            return ResponseEntity.ok(designerService.addRelatedQuestion(id, relatedId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/questions/{id}/related/{relatedId}")
    public ResponseEntity<?> removeRelatedQuestion(
            @PathVariable Long id,
            @PathVariable Long relatedId) {
        try {
            return ResponseEntity.ok(designerService.removeRelatedQuestion(id, relatedId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Categories endpoints
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        try {
            return ResponseEntity.ok(designerService.getCategories());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryRequest request) {
        try {
            return ResponseEntity.ok(designerService.createCategory(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequest request) {
        try {
            return ResponseEntity.ok(designerService.updateCategory(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            designerService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.quiz.service.PlayerService;
import com.example.quiz.service.SocialService;
import com.example.quiz.dto.request.AnswerRequest;

@RestController
@RequestMapping("/api/player")
@PreAuthorize("hasRole('PLAYER')")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SocialService socialService;

    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer difficulty) {
        try {
            return ResponseEntity.ok(playerService.getQuestions(category, difficulty));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/questions/random")
    public ResponseEntity<?> getRandomQuestion() {
        try {
            return ResponseEntity.ok(playerService.getRandomQuestions());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/questions/{id}/answer")
    public ResponseEntity<?> submitAnswer(
            @PathVariable Long id,
            @RequestBody AnswerRequest request) {
        try {
            return ResponseEntity.ok(playerService.submitAnswer(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        try {
            return ResponseEntity.ok(playerService.getLeaderboard());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/follow/designer/{id}")
    public ResponseEntity<?> followDesigner(@PathVariable Long id) {
        try {
            socialService.followDesigner(id);
            return ResponseEntity.ok().body("Designer followed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/follow/player/{id}")
    public ResponseEntity<?> followPlayer(@PathVariable Long id) {
        try {
            socialService.followPlayer(id);
            return ResponseEntity.ok().body("Player followed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/follow/designer/{id}")
    public ResponseEntity<?> unfollowDesigner(@PathVariable Long id) {
        try {
            socialService.unfollowDesigner(id);
            return ResponseEntity.ok().body("Designer unfollowed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/follow/player/{id}")
    public ResponseEntity<?> unfollowPlayer(@PathVariable Long id) {
        try {
            socialService.unfollowPlayer(id);
            return ResponseEntity.ok().body("Player unfollowed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
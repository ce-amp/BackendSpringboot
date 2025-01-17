package com.example.quiz.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    
    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    public boolean isCurrentUser(Long userId) {
        return getCurrentUsername().equals(userId.toString());
    }
}
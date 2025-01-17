package com.example.quiz.service;

import com.example.quiz.entity.User;
import com.example.quiz.entity.Role;
import com.example.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class SocialService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void followDesigner(Long designerId) {
        User currentUser = userService.getCurrentUser();
        User designer = userRepository.findByIdAndRole(designerId, Role.DESIGNER)
            .orElseThrow(() -> new RuntimeException("Designer not found"));

        if (currentUser.getId().equals(designerId)) {
            throw new RuntimeException("Cannot follow yourself");
        }

        currentUser.getFollowing().add(designer);
        userRepository.save(currentUser);
    }

    @Transactional
    public void followPlayer(Long playerId) {
        User currentUser = userService.getCurrentUser();
        User player = userRepository.findByIdAndRole(playerId, Role.PLAYER)
            .orElseThrow(() -> new RuntimeException("Player not found"));

        if (currentUser.getId().equals(playerId)) {
            throw new RuntimeException("Cannot follow yourself");
        }

        currentUser.getFollowing().add(player);
        userRepository.save(currentUser);
    }

    @Transactional
    public void unfollowDesigner(Long designerId) {
        User currentUser = userService.getCurrentUser();
        User designer = userRepository.findByIdAndRole(designerId, Role.DESIGNER)
            .orElseThrow(() -> new RuntimeException("Designer not found"));

        currentUser.getFollowing().remove(designer);
        userRepository.save(currentUser);
    }

    @Transactional
    public void unfollowPlayer(Long playerId) {
        User currentUser = userService.getCurrentUser();
        User player = userRepository.findByIdAndRole(playerId, Role.PLAYER)
            .orElseThrow(() -> new RuntimeException("Player not found"));

        currentUser.getFollowing().remove(player);
        userRepository.save(currentUser);
    }
}
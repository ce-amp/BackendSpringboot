package com.example.quiz.service;

import com.example.quiz.dto.request.UserUpdateRequest;
import com.example.quiz.dto.response.UserResponse;
import com.example.quiz.entity.Role;
import com.example.quiz.entity.User;
import com.example.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Current user not found"));
    }

    public UserResponse getCurrentUserProfile() {
        return mapToUserResponse(getCurrentUser(), true);
    }

    public UserResponse updateProfile(UserUpdateRequest request) {
        User user = getCurrentUser();

        // Validate username uniqueness if it's being changed
        if (!user.getUsername().equals(request.getUsername())) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username already taken");
            }
            user.setUsername(request.getUsername());
        }

        user = userRepository.save(user);
        return mapToUserResponse(user, true);
    }

    public UserResponse getUserProfile(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user, true);
    }

    public List<UserResponse> getCurrentUserFollowing() {
        return getCurrentUser().getFollowing().stream()
            .map(user -> mapToUserResponse(user, false))
            .collect(Collectors.toList());
    }

    public List<UserResponse> getCurrentUserFollowers() {
        return getCurrentUser().getFollowers().stream()
            .map(user -> mapToUserResponse(user, false))
            .collect(Collectors.toList());
    }

    public List<UserResponse> getAllUsers(String roleStr) {
        List<User> users;
        if (roleStr != null) {
            try {
                Role role = Role.valueOf(roleStr.toUpperCase());
                users = userRepository.findByRoleOrderByUsernameAsc(role);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role specified");
            }
        } else {
            users = userRepository.findAllByOrderByUsernameAsc();
        }

        return users.stream()
            .map(user -> mapToUserResponse(user, false))
            .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user, boolean includeDetails) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setPoints(user.getPoints());

        if (includeDetails) {
            response.setFollowing(user.getFollowing().stream()
                .map(following -> mapToUserResponse(following, false))
                .collect(Collectors.toList()));
            response.setFollowers(user.getFollowers().stream()
                .map(follower -> mapToUserResponse(follower, false))
                .collect(Collectors.toList()));
        }

        return response;
    }
}
package com.example.quiz.service;

import com.example.quiz.config.JwtTokenProvider;
import com.example.quiz.dto.request.AuthRequest;
import com.example.quiz.dto.response.AuthResponse;
import com.example.quiz.dto.response.UserResponse;
import com.example.quiz.entity.Role;
import com.example.quiz.entity.User;
import com.example.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(AuthRequest request) {
        // Validate username
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Validate role
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Must be either DESIGNER or PLAYER");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setPoints(0);

        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenProvider.createToken(
            user.getUsername(),
            user.getRole().name()
        );

        return new AuthResponse(
            token,
            mapToUserResponse(user),
            "User registered successfully"
        );
    }

    public AuthResponse login(AuthRequest request) {
        // Find user
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate JWT token
        String token = jwtTokenProvider.createToken(
            user.getUsername(),
            user.getRole().name()
        );

        return new AuthResponse(
            token,
            mapToUserResponse(user),
            "Login successful"
        );
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setPoints(user.getPoints());
        return response;
    }
}
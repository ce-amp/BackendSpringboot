package com.example.quiz.repository;

import com.example.quiz.entity.User;
import com.example.quiz.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRoleOrderByUsernameAsc(Role role);
    List<User> findAllByOrderByUsernameAsc();
    List<User> findTop10ByRoleOrderByPointsDesc(Role role);
    Optional<User> findByIdAndRole(Long id, Role role);
}
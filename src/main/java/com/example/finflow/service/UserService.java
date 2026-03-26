package com.example.finflow.service;

import com.example.finflow.model.User;
import com.example.finflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));
    }

    public User createUser(String email, String name, String passwordHash) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Пользователь с email " + email + " уже существует");
        }
        User user = new User(email, name, passwordHash);
        return userRepository.save(user);
    }
}
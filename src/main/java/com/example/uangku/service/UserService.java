package com.example.uangku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.uangku.dto.UserLoginDTO;
import com.example.uangku.dto.UserRegisterDTO;
import com.example.uangku.model.User;
import com.example.uangku.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    public User login(UserLoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        return user;
    }
}

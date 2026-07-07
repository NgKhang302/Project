package com.eduapp.service;

import com.eduapp.dto.UserDTO;
import com.eduapp.exception.ResourceNotFoundException;
import com.eduapp.exception.ValidationException;
import com.eduapp.model.User;
import com.eduapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getProfile(String username) {
        return toDTO(findByUsername(username));
    }

    public UserDTO updateProfile(String username, String newEmail) {
        User user = findByUsername(username);

        if (newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new ValidationException("Email is already registered");
            }
            user.setEmail(newEmail);
        }

        return toDTO(userRepository.save(user));
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

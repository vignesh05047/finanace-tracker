package com.example.financetracker.service;

import com.example.financetracker.model.User;
import com.example.financetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get all users (ADMIN only)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Update role
    public String updateRole(Long id, User.Role newRole) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()) return "User not found";
        User user = optional.get();
        user.setRole(newRole);
        userRepository.save(user);
        return "Role updated to " + newRole;
    }

    // Toggle active/inactive status
    public String toggleStatus(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()) return "User not found";
        User user = optional.get();
        user.setActive(!user.isActive());
        userRepository.save(user);
        return "User is now " + (user.isActive() ? "active" : "inactive");
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Login
    public String login(String username, String password) {
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isEmpty()) return "User not found";
        User user = optional.get();
        if (!user.isActive()) return "Account is inactive";
        if (!user.getPassword().equals(password)) return "Invalid password";
        return "Login successful | role=" + user.getRole();
    }
}
package com.example.financetracker.controller;

import com.example.financetracker.model.User;
import com.example.financetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // POST /users — create user (ADMIN only enforced via header check)
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user,
                                        @RequestHeader("X-Role") String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Only ADMIN can create users"));
        }
        return ResponseEntity.ok(userService.createUser(user));
    }

    // GET /users — get all users (ADMIN only)
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader("X-Role") String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Only ADMIN can view all users"));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /users/{id}/role — update role (ADMIN only)
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id,
                                        @RequestParam String newRole,
                                        @RequestHeader("X-Role") String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Only ADMIN can change roles"));
        }
        try {
            User.Role parsedRole = User.Role.valueOf(newRole.toUpperCase());
            return ResponseEntity.ok(Map.of("message", userService.updateRole(id, parsedRole)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role. Use VIEWER, ANALYST, or ADMIN"));
        }
    }

    // PATCH /users/{id}/status — toggle active/inactive (ADMIN only)
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id,
                                          @RequestHeader("X-Role") String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Only ADMIN can change user status"));
        }
        return ResponseEntity.ok(Map.of("message", userService.toggleStatus(id)));
    }

    // DELETE /users/{id} (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                        @RequestHeader("X-Role") String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Only ADMIN can delete users"));
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    // POST /users/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String result = userService.login(body.get("username"), body.get("password"));
        if (result.startsWith("Login successful")) {
            return ResponseEntity.ok(Map.of("message", result));
        }
        return ResponseEntity.status(401).body(Map.of("error", result));
    }
}
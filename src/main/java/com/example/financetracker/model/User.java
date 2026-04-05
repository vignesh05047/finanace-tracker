package com.example.financetracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;   // plain text is fine

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;         // VIEWER, ANALYST, ADMIN

    @Column(nullable = false)
    private boolean active = true;

    public enum Role {
        VIEWER, ANALYST, ADMIN
    }
}
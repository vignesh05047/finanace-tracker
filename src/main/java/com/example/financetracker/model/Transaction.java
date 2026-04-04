package com.example.financetracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "type is required (income or expense)")
    private String type;

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "role is required")
    private String role;

    @Positive(message = "amount must be greater than zero")
    private double amount;

    @NotBlank(message = "category is required")
    private String category;

    @NotBlank(message = "date is required")
    private String date;

}
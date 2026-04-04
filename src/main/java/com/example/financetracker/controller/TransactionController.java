package com.example.financetracker.controller;

import com.example.financetracker.model.Transaction;
import com.example.financetracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public String add(@RequestBody @Valid  Transaction t) {

        if (!t.getRole().equalsIgnoreCase("ADMIN")) {
            return "Access Denied: Only ADMIN can add";
        }

        service.save(t);
        return "Transaction added successfully";
    }

    @GetMapping
    public List<Transaction> getTransactions(
            @RequestParam String userId,
            @RequestParam String role) {

        return service.getTransactions(userId, role);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam(required = false) String role) {

        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            return "Access Denied: Only ADMIN can delete";
        }

        service.delete(id);
        return "Deleted successfully";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestBody @Valid   Transaction updatedTransaction,
                         @RequestParam(required = false) String role) {

        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            return "Access Denied: Only ADMIN can update";
        }

        return service.update(id, updatedTransaction);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(service.getDashboard());
    }
    @GetMapping("/filter")
    public List<Transaction> filter(
            @RequestParam String type,
            @RequestParam String category) {

        return service.filter(type, category);
    }
    @GetMapping("/filter/date")
    public ResponseEntity<?> filterByDate(@RequestParam String date) {
        return ResponseEntity.ok(service.filterByDate(date));
    }
}
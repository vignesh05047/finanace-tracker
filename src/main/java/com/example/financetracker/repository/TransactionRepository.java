package com.example.financetracker.repository;

import com.example.financetracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(String userId);
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'income'")
    Double getTotalIncome();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'expense'")
    Double getTotalExpense();
    List<Transaction> findByTypeAndCategory(String type, String category);
    List<Transaction> findByDate(String date);
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t GROUP BY t.category")
    List<Object[]> getTotalsByCategory();

    List<Transaction> findTop5ByOrderByIdDesc();  // recent activity
}

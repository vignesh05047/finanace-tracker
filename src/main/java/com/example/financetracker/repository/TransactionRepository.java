package com.example.financetracker.repository;

import com.example.financetracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    List<Transaction> findTop5ByOrderByIdDesc();

    @Query("SELECT t.date, SUM(t.amount) FROM Transaction t WHERE t.type = :type GROUP BY t.date ORDER BY t.date DESC")
    List<Object[]> getMonthlyTrends(@Param("type") String type);
}
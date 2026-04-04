package com.example.financetracker.service;

import com.example.financetracker.model.Transaction;
import com.example.financetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repo;

    public Transaction save(Transaction t) {
        return repo.save(t);
    }

    public List<Transaction> getTransactions(String userId, String role) {
        if (role.equalsIgnoreCase("ADMIN")) {
            return repo.findAll();
        } else {
            return repo.findByUserId(userId);
        }
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public String update(Long id, Transaction updatedTransaction) {
        Optional<Transaction> optional = repo.findById(id);
        if (optional.isEmpty()) {
            return "Transaction not found";
        }
        Transaction existing = optional.get();
        existing.setType(updatedTransaction.getType());
        existing.setAmount(updatedTransaction.getAmount());
        existing.setCategory(updatedTransaction.getCategory());
        existing.setDate(updatedTransaction.getDate());
        existing.setNotes(updatedTransaction.getNotes());
        repo.save(existing);
        return "Updated successfully";
    }

    public Map<String, Object> getDashboard() {

        Double income = repo.getTotalIncome();
        Double expense = repo.getTotalExpense();
        double inc = income != null ? income : 0;
        double exp = expense != null ? expense : 0;

        Map<String, Double> byCategory = new LinkedHashMap<>();
        repo.getTotalsByCategory()
                .forEach(row -> byCategory.put((String) row[0], (Double) row[1]));

        List<Transaction> recent = repo.findTop5ByOrderByIdDesc();

        List<Object[]> trendRows = repo.getMonthlyTrends("income");
        Map<String, Double> trends = new LinkedHashMap<>();
        trendRows.forEach(row -> trends.put((String) row[0], (Double) row[1]));

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalIncome", inc);
        data.put("totalExpense", exp);
        data.put("netBalance", inc - exp);
        data.put("byCategory", byCategory);
        data.put("recentActivity", recent);
        data.put("monthlyTrends", trends);

        return data;
    }

    public List<Transaction> filter(String type, String category) {
        return repo.findByTypeAndCategory(type, category);
    }

    public List<Transaction> filterByDate(String date) {
        return repo.findByDate(date);
    }
}
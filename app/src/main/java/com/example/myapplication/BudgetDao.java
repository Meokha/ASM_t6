package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entity.Budget;

import java.util.List;

@Dao
public interface BudgetDao {


    @Query("SELECT * FROM budget ORDER BY createdAt DESC")
    List<Budget> getAllBudgets();

    @Insert
    void insertBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);

    @Query("DELETE FROM budget")
    void deleteAll();

    @Delete
    void delete(Budget budget);
    @Query("SELECT * FROM budget WHERE name = :budgetName LIMIT 1")
    Budget findBudgetByName(String budgetName);
    @Query("SELECT * FROM budget WHERE id = :id LIMIT 1")
    Budget findById(int id);
    @Query("UPDATE budget SET spentAmount = spentAmount - :amountToSubtract WHERE name = :budgetName")
    void subtractSpentAmount(String budgetName, double amountToSubtract);
    // <-- THÊM HÀM MỚI NÀY VÀO
    @Query("UPDATE budget SET spentAmount = spentAmount + :spentNow WHERE name = :budgetName")
    void updateSpentAmount(String budgetName, double spentNow);
}
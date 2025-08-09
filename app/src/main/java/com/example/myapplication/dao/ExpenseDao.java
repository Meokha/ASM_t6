package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;


import com.example.myapplication.entity.Expense;

import java.util.List;
@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);
    @Query("SELECT * FROM expense ORDER BY createdAt DESC")
    List<Expense> getAllExpenses();
    @Query("DELETE FROM expense")
    void deleteAll();
    @Delete
    void delete(Expense expense);
    @Update
    void update(Expense expense);


}

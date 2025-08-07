package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date; // ✅ đúng chỗ

@Entity(tableName = "expense")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long createdAt;

    private Date date;
    private String category;
    private String title;
    private double amount;
    private String description;


    public Expense(String title, double amount, String description, long createdAt) {
        this.title = title;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

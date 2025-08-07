package com.example.myapplication.entity;
import androidx.room.Ignore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget")
public class Budget {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double amount;
    private long createdAt;

    private String description; // ✅ THÊM MỚI

    // ✅ Constructor không tham số cho Room
    public Budget() {
    }
    @Ignore
    // ✅ Constructor có tham số (nếu cần)
    public Budget(String name, double amount, long createdAt, String description) {
        this.name = name;
        this.amount = amount;
        this.createdAt = createdAt;
        this.description = description;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public String getDescription() { return description; } // ✅
    public void setDescription(String description) { this.description = description; } // ✅
}

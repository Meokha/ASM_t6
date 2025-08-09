package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget")
public class Budget {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;    // Tên của ngân sách, ví dụ: "Food", "Transport"
    private double amount;  // Tổng số tiền của ngân sách

    // <-- THÊM MỚI: Cột này để lưu số tiền đã chi tiêu cho ngân sách này
    private double spentAmount;
    private long budgetDate;
    private long createdAt;
    private String description;

    // Constructor không tham số cho Room
    public Budget() {
        // Room cần constructor này
    }

    // Constructor có tham số để bạn tự tạo đối tượng
    // @Ignore để Room không sử dụng constructor này
    @Ignore
    public Budget(String name, double amount, long createdAt, String description) {
        this.name = name;
        this.amount = amount;
        this.createdAt = createdAt;
        this.description = description;
        this.budgetDate = budgetDate;
        this.spentAmount = 0; // <-- QUAN TRỌNG: Khởi tạo giá trị đã chi là 0 khi tạo mới
    }

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // <-- THÊM MỚI: Getter và Setter cho spentAmount
    public double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }
    public long getBudgetDate() {
        return budgetDate;
    }

    public void setBudgetDate(long budgetDate) {
        this.budgetDate = budgetDate;
    }
}
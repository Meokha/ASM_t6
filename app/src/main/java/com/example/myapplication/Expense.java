package com.example.myapplication.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "expense")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private double amount;
    private String description;
    private String category; // Tên của budget liên quan
    private Date date;       // Ngày thực hiện chi tiêu
    private long createdAt;  // Thời gian tạo bản ghi

    // --- CONSTRUCTORS ---

    /**
     * Constructor rỗng (không tham số).
     * Room cần constructor này để có thể tạo các đối tượng Entity.
     */
    public Expense() {
    }

    /**
     * Constructor đầy đủ để chúng ta sử dụng trong code khi tạo một khoản chi mới.
     * @Ignore báo cho Room bỏ qua, không sử dụng constructor này.
     */
    @Ignore
    public Expense(String title, double amount, String description, String category, Date date) {
        this.title = title;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
        this.createdAt = System.currentTimeMillis(); // Tự động gán thời gian tạo khi có chi tiêu mới
    }

    /*
     * Constructor cũ của bạn đã được thay thế bằng constructor đầy đủ ở trên.
     * Bạn có thể xóa nó đi để code gọn hơn.
     *
     * public Expense(String title, double amount, String description, long createdAt) {
     *     this.title = title;
     *     this.amount = amount;
     *     this.description = description;
     *     this.createdAt = createdAt;
     * }
     */


    // --- Getters & Setters ---
    // (Không thay đổi, giữ nguyên tất cả)

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
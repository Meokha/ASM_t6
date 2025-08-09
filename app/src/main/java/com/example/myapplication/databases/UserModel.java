package com.example.myapplication.databases;

public class UserModel {
    //
    private int id;

    private String username;

    private String email;

    private  String phone;

    private int role;
    private String password;
    private String createdAt;

    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPassword() {
        return password; // <-- Lệnh return đã được thêm vào
    }
    /**
     * Hàm này thiết lập mật khẩu mới cho người dùng.
     * @param password Mật khẩu mới
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
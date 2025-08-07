package com.example.myapplication.databases;

public class BudgetModel {
    private int id;
    private String nameBudget;
    private int moneyBudget;
    private int statusBudget;
    private String description;
    private String createdAt;
    private String updatedAt;

    public BudgetModel(int id, String nameBudget, int moneyBudget, int statusBudget, String description, String createdAt, String updatedAt) {
        this.id = id;
        this.nameBudget = nameBudget;
        this.moneyBudget = moneyBudget;
        this.statusBudget = statusBudget;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameBudget() {
        return nameBudget;
    }

    public void setNameBudget(String nameBudget) {
        this.nameBudget = nameBudget;
    }

    public int getMoneyBudget() {
        return moneyBudget;
    }

    public void setMoneyBudget(int moneyBudget) {
        this.moneyBudget = moneyBudget;
    }

    public int getStatusBudget() {
        return statusBudget;
    }

    public void setStatusBudget(int statusBudget) {
        this.statusBudget = statusBudget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

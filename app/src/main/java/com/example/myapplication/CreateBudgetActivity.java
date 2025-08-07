package com.example.myapplication.budgets;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.entity.Budget;

public class CreateBudgetActivity extends AppCompatActivity {

    private EditText edtBudgetName, edtBudgetMoney, edtBudgetDercription;
    private Button btnSaveBudget, btnBackBudget;

    private int budgetId = -1; // Nếu là -1 => tạo mới, nếu khác => chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        edtBudgetName = findViewById(R.id.edtBudgetName);
        edtBudgetMoney = findViewById(R.id.edtBudgetMoney);
        edtBudgetDercription = findViewById(R.id.edtBudgetDercription);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        btnBackBudget = findViewById(R.id.btnBackBudget);

        // Kiểm tra xem có dữ liệu ID được truyền vào không (=> edit)
        if (getIntent().hasExtra("BUDGET_ID")) {
            budgetId = getIntent().getIntExtra("BUDGET_ID", -1);
            loadBudgetToEdit(budgetId);
        }

        btnSaveBudget.setOnClickListener(v -> saveBudget());
        btnBackBudget.setOnClickListener(v -> finish());
    }

    private void loadBudgetToEdit(int id) {
        Budget budget = AppDatabase.getInstance(this).budgetDao().findById(id);
        if (budget != null) {
            edtBudgetName.setText(budget.getName());
            edtBudgetMoney.setText(String.valueOf(budget.getAmount()));
            edtBudgetDercription.setText(budget.getDescription());
            setTitle("Edit Budget");
        }
    }

    private void saveBudget() {
        String name = edtBudgetName.getText().toString().trim();
        String amountStr = edtBudgetMoney.getText().toString().trim();
        String description = edtBudgetDercription.getText().toString().trim();

        if (name.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Name and amount are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);

        if (budgetId != -1) {
            // Cập nhật budget cũ
            Budget budget = db.budgetDao().findById(budgetId);
            if (budget != null) {
                budget.setName(name);
                budget.setAmount(amount);
                budget.setDescription(description);
                db.budgetDao().updateBudget(budget);
                Toast.makeText(this, "Budget updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Tạo mới
            Budget budget = new Budget();
            budget.setName(name);
            budget.setAmount(amount);
            budget.setDescription(description);
            budget.setCreatedAt(System.currentTimeMillis());
            db.budgetDao().insertBudget(budget);
            Toast.makeText(this, "Budget created", Toast.LENGTH_SHORT).show();
        }

        finish(); // Quay lại BudgetFragment
    }
}

package com.example.myapplication.budgets;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // <-- THÊM MỚI
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.AppDatabase;
import com.example.myapplication.R;
import com.example.myapplication.entity.Budget;

import java.text.SimpleDateFormat; // <-- THÊM MỚI
import java.util.Calendar;       // <-- THÊM MỚI
import java.util.Date;          // <-- THÊM MỚI
import java.util.Locale;        // <-- THÊM MỚI
import java.util.concurrent.ExecutorService; // <-- THÊM MỚI
import java.util.concurrent.Executors;    // <-- THÊM MỚI


public class CreateBudgetActivity extends AppCompatActivity {

    private EditText edtBudgetName, edtBudgetMoney, edtBudgetDercription;
    private Button btnSaveBudget, btnBackBudget;
    private TextView tvBudgetDate; // <-- THÊM MỚI

    // Khởi tạo là null để bắt buộc người dùng chọn ngày
    private Date selectedBudgetDate = null; // <-- THÊM MỚI

    private int budgetId = -1;
    private AppDatabase db; // <-- THÊM MỚI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        db = AppDatabase.getInstance(this); // Khởi tạo db
        edtBudgetName = findViewById(R.id.edtBudgetName);
        edtBudgetMoney = findViewById(R.id.edtBudgetMoney);
        edtBudgetDercription = findViewById(R.id.edtBudgetDercription);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        btnBackBudget = findViewById(R.id.btnBackBudget);

        // --- THÊM MỚI: Ánh xạ và xử lý cho TextView chọn ngày ---
        tvBudgetDate = findViewById(R.id.tvBudgetDate); // Nhớ thêm ID này vào XML
        tvBudgetDate.setOnClickListener(v -> showDatePicker());
        // --- KẾT THÚC PHẦN THÊM MỚI ---

        // Kiểm tra xem có intent để chỉnh sửa không
        if (getIntent().hasExtra("BUDGET_ID")) {
            budgetId = getIntent().getIntExtra("BUDGET_ID", -1);
            loadBudgetToEdit(budgetId);
        }

        btnSaveBudget.setOnClickListener(v -> saveBudget());
        btnBackBudget.setOnClickListener(v -> finish());
    }

    private void loadBudgetToEdit(int id) {
        // Thực hiện truy vấn trên luồng nền
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Budget budget = db.budgetDao().findById(id);
            if (budget != null) {
                // Quay lại UI thread để cập nhật giao diện
                runOnUiThread(() -> {
                    edtBudgetName.setText(budget.getName());
                    edtBudgetMoney.setText(String.valueOf(budget.getAmount()));
                    edtBudgetDercription.setText(budget.getDescription());
                    setTitle("Edit Budget");

                    // --- THÊM MỚI: Hiển thị ngày đã lưu của budget ---
                    if (budget.getBudgetDate() > 0) {
                        selectedBudgetDate = new Date(budget.getBudgetDate());
                        updateDateText(); // Gọi hàm để hiển thị ngày lên TextView
                    }
                    // --- KẾT THÚC PHẦN THÊM MỚI ---
                });
            }
        });
    }

    private void saveBudget() {
        String name = edtBudgetName.getText().toString().trim();
        String amountStr = edtBudgetMoney.getText().toString().trim();
        String description = edtBudgetDercription.getText().toString().trim();

        if (name.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Name and amount cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- THÊM MỚI: Kiểm tra xem người dùng đã chọn ngày chưa ---
        if (selectedBudgetDate == null) {
            Toast.makeText(this, "Please select a date for your budget.", Toast.LENGTH_SHORT).show();
            return;
        }
        // --- KẾT THÚC PHẦN THÊM MỚI ---

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện lưu/cập nhật trên luồng nền
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String message;
            if (budgetId != -1) {
                // Cập nhật budget cũ
                Budget budget = db.budgetDao().findById(budgetId);
                if (budget != null) {
                    budget.setName(name);
                    budget.setAmount(amount);
                    budget.setDescription(description);
                    budget.setBudgetDate(selectedBudgetDate.getTime()); // Cập nhật ngày
                    db.budgetDao().updateBudget(budget);
                    message = "Budget updated!";
                } else {
                    message = "Error: No budget found!";
                }
            } else {
                // Tạo mới
                Budget budget = new Budget();
                budget.setName(name);
                budget.setAmount(amount);
                budget.setDescription(description);
                budget.setCreatedAt(System.currentTimeMillis());
                budget.setBudgetDate(selectedBudgetDate.getTime()); // Thêm ngày
                db.budgetDao().insertBudget(budget);
                message = "Budget created successfully!";
            }

            // Quay lại UI thread để hiển thị thông báo và kết thúc
            String finalMessage = message;
            runOnUiThread(() -> {
                Toast.makeText(this, finalMessage, Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    // --- THÊM CÁC HÀM MỚI ĐỂ XỬ LÝ DATEPICKER ---
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        if (selectedBudgetDate != null) {
            calendar.setTime(selectedBudgetDate);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedBudgetDate = calendar.getTime();
                    updateDateText();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void updateDateText() {
        if (selectedBudgetDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvBudgetDate.setText(sdf.format(selectedBudgetDate));
        }
    }
}
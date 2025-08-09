package com.example.myapplication; // Hoặc package tương ứng của bạn

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.entity.Budget;
import com.example.myapplication.entity.Expense;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etTitle, etAmount, etDescription;
    private Button btnSave;
    private TextView tvDate;
    private Spinner spinnerCategory;
    private Date selectedDate;
    private AppDatabase db;
    private List<String> budgetCategories = new ArrayList<>();
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        db = AppDatabase.getInstance(this);

        etTitle = findViewById(R.id.et_title);
        etAmount = findViewById(R.id.et_amount);
        etDescription = findViewById(R.id.et_description);
        btnSave = findViewById(R.id.btn_save);
        tvDate = findViewById(R.id.tv_date);
        spinnerCategory = findViewById(R.id.spinner_category);

        selectedDate = new Date();
        updateDateText();
        tvDate.setOnClickListener(v -> showDatePicker());

        setupCategorySpinner();
        btnSave.setOnClickListener(v -> attemptToSaveExpense());
    }

    private void attemptToSaveExpense() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề và số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerCategory.getSelectedItem() == null) {
            Toast.makeText(this, "Vui lòng chọn một ngân sách", Toast.LENGTH_SHORT).show();
            return;
        }
        String selectedBudgetName = spinnerCategory.getSelectedItem().toString();

        double newExpenseAmount;
        try {
            newExpenseAmount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Budget targetBudget = db.budgetDao().findBudgetByName(selectedBudgetName);

            if (targetBudget == null) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi: Không tìm thấy ngân sách", Toast.LENGTH_SHORT).show());
                return;
            }

            double currentSpent = targetBudget.getSpentAmount();
            double totalLimit = targetBudget.getAmount();

            if (currentSpent + newExpenseAmount > totalLimit) {
                double remaining = totalLimit - currentSpent;
                String errorMessage = String.format(
                        "Không đủ ngân sách! Bạn chỉ còn lại %s.",
                        currencyFormatter.format(remaining)
                );
                runOnUiThread(() -> Toast.makeText(AddExpenseActivity.this, errorMessage, Toast.LENGTH_LONG).show());
            } else {
                // --- THAY ĐỔI DUY NHẤT Ở ĐÂY ---
                // Dòng cũ (SAI):
                // Expense newExpense = new Expense(title, newExpenseAmount, description, System.currentTimeMillis());
                // newExpense.setDate(selectedDate);
                // newExpense.setCategory(selectedBudgetName);

                // Dòng mới (ĐÚNG):
                Expense newExpense = new Expense(title, newExpenseAmount, description, selectedBudgetName, selectedDate);
                // --- KẾT THÚC THAY ĐỔI ---


                // Lưu expense mới
                db.expenseDao().insert(newExpense);
                // Cập nhật lại budget
                db.budgetDao().updateSpentAmount(selectedBudgetName, newExpenseAmount);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã lưu chi tiêu thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    // --- CÁC HÀM CŨ GIỮ NGUYÊN ---
    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                budgetCategories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Budget> budgets = db.budgetDao().getAllBudgets();
            budgetCategories.clear();
            for (Budget budget : budgets) {
                budgetCategories.add(budget.getName());
            }

            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                if (budgetCategories.isEmpty()) {
                    Toast.makeText(this, "Vui lòng tạo một ngân sách trước!", Toast.LENGTH_LONG).show();
                    btnSave.setEnabled(false);
                }
            });
        });
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(selectedDate));
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedDate = calendar.getTime();
                    updateDateText();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
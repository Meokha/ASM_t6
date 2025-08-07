package com.example.myapplication;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication.entity.Expense;
import java.util.Date;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
import android.app.DatePickerDialog;




import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etTitle, etAmount, etDescription;
    private Button btnSave;
    private TextView tvDate;
    private Date selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        tvDate = findViewById(R.id.tv_date);
        selectedDate = new Date(); // default: hôm nay
        updateDateText(); // hiển thị ngày lên TextView

        tvDate.setOnClickListener(v -> showDatePicker());


        etTitle = findViewById(R.id.et_title);
        etAmount = findViewById(R.id.et_amount);
        etDescription = findViewById(R.id.et_description);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
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
                    updateDateText(); // cập nhật lại TextView
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }



    private void saveExpense() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please enter title and amount", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDate == null) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }


        double amount = Double.parseDouble(amountStr);
        long createdAt = System.currentTimeMillis();

        Expense expense = new Expense(title, amount, description, createdAt);
        expense.setDate(selectedDate); // ✅ dùng ngày người dùng đã chọn
        expense.setCategory("Uncategorized"); // Hoặc tạo dropdown/spinner chọn danh mục


        // Lưu vào Room DB
        AppDatabase.getInstance(this).expenseDao().insert(expense);

        Toast.makeText(this, "Expense saved: " + title + " ($" + amount + ")", Toast.LENGTH_LONG).show();

        finish();
    }

}

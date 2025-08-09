package com.example.myapplication;

import android.app.DatePickerDialog; // <-- THÊM MỚI: Import cần thiết
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AppDatabase;
import com.example.myapplication.entity.Expense;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar; // <-- THÊM MỚI: Import cần thiết
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private List<Expense> expenseList;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvTitle.setText(expense.getTitle());
        holder.tvAmount.setText(currencyFormatter.format(expense.getAmount()));
        holder.tvDescription.setText(expense.getDescription());

        Date date = expense.getDate();
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvDate.setText(sdf.format(date));
        } else {
            holder.tvDate.setText("No date");
        }

        // Xử lý nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Spending")
                    .setMessage("Are you sure you want to delete this expense?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteExpenseInBackground(expense, position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Xử lý nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            showEditExpenseDialog(expense, position);
        });
    }

    private void deleteExpenseInBackground(Expense expense, int position) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            com.example.myapplication.AppDatabase db = com.example.myapplication.AppDatabase.getInstance(context);

            String budgetName = expense.getCategory();
            double expenseAmount = expense.getAmount();

            if (budgetName != null && !budgetName.isEmpty()) {
                db.budgetDao().updateSpentAmount(budgetName, -expenseAmount); // Trừ đi số tiền đã chi
            }

            db.expenseDao().delete(expense);

            ((MainMenuActivity) context).runOnUiThread(() -> {
                expenseList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, expenseList.size());
                Toast.makeText(context, "Deleted spending", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void showEditExpenseDialog(Expense expense, int position) {
        // Sử dụng Builder để tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_expense, null);

        EditText etTitle = dialogView.findViewById(R.id.et_edit_title);
        EditText etAmount = dialogView.findViewById(R.id.et_edit_amount);
        EditText etDescription = dialogView.findViewById(R.id.et_edit_description);
        TextView tvDate = dialogView.findViewById(R.id.tv_edit_date);
        final Calendar selectedDate = Calendar.getInstance();

        // Điền dữ liệu cũ vào dialog
        etTitle.setText(expense.getTitle());
        etAmount.setText(String.valueOf(expense.getAmount()));
        etDescription.setText(expense.getDescription());

        // Xử lý ngày tháng
        if (expense.getDate() != null) {
            selectedDate.setTime(expense.getDate());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(selectedDate.getTime()));

        tvDate.setOnClickListener(v -> {
            int year = selectedDate.get(Calendar.YEAR);
            int month = selectedDate.get(Calendar.MONTH);
            int day = selectedDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year1, month1, dayOfMonth) -> {
                selectedDate.set(year1, month1, dayOfMonth);
                tvDate.setText(sdf.format(selectedDate.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });

        // Cấu hình và hiển thị dialog
        builder.setTitle("Edit spending")
                .setView(dialogView)
                .setPositiveButton("Update", (dialogInterface, which) -> {
                    String newTitle = etTitle.getText().toString().trim();
                    String newAmountStr = etAmount.getText().toString().trim();
                    String newDescription = etDescription.getText().toString().trim();
                    Date newDate = selectedDate.getTime(); // Lấy ngày mới từ Calendar

                    if (!newTitle.isEmpty() && !newAmountStr.isEmpty()) {
                        try {
                            double newAmount = Double.parseDouble(newAmountStr);
                            // <-- SỬA LẠI: Truyền cả newDate vào hàm cập nhật
                            updateExpenseInBackground(expense, position, newTitle, newAmount, newDescription, newDate);
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Please enter complete information", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show(); // Hiển thị dialog
    }

    // <-- SỬA LẠI: Thêm tham số Date newDate vào hàm
    private void updateExpenseInBackground(Expense oldExpense, int position, String newTitle, double newAmount, String newDescription, Date newDate) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            com.example.myapplication.AppDatabase db = com.example.myapplication.AppDatabase.getInstance(context);

            double oldAmount = oldExpense.getAmount();
            String budgetName = oldExpense.getCategory();

            double amountDifference = newAmount - oldAmount;

            if (budgetName != null && !budgetName.isEmpty()) {
                db.budgetDao().updateSpentAmount(budgetName, amountDifference);
            }

            // Cập nhật lại thông tin cho expense
            oldExpense.setTitle(newTitle);
            oldExpense.setAmount(newAmount);
            oldExpense.setDescription(newDescription);
            oldExpense.setDate(newDate); // <-- THÊM DÒNG NÀY: Cập nhật ngày mới cho đối tượng

            db.expenseDao().update(oldExpense);

            ((MainMenuActivity) context).runOnUiThread(() -> {
                notifyItemChanged(position);
                Toast.makeText(context, "Expense updated successfully", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAmount, tvDescription, tvDate;
        ImageView btnDelete, btnEdit;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    public void updateData(List<Expense> newList) {
        expenseList = newList;
        notifyDataSetChanged();
    }
}
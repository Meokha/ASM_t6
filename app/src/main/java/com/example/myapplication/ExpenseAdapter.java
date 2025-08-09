package com.example.myapplication;

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

import com.example.myapplication.entity.Expense;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
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
            holder.tvDate.setText("Không có ngày");
        }

        // Xử lý nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa Chi Tiêu")
                    .setMessage("Bạn có chắc muốn xóa khoản chi này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // <-- SỬA LẠI: Gọi hàm xóa trên luồng nền
                        deleteExpenseInBackground(expense, position);
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        // Xử lý nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            showEditExpenseDialog(expense, position);
        });
    }

    // <-- THÊM MỚI: Hàm xóa trên luồng nền
    private void deleteExpenseInBackground(Expense expense, int position) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);

            String budgetName = expense.getCategory();
            double expenseAmount = expense.getAmount();

            // 1. TRỪ tiền khỏi budget tương ứng
            if (budgetName != null && !budgetName.isEmpty()) {
                db.budgetDao().subtractSpentAmount(budgetName, expenseAmount);
            }

            // 2. Xóa expense
            db.expenseDao().delete(expense);

            // Cập nhật giao diện trên UI Thread
            ((MainMenuActivity) context).runOnUiThread(() -> {
                expenseList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, expenseList.size());
                Toast.makeText(context, "Đã xóa chi tiêu", Toast.LENGTH_SHORT).show();
            });
        });
    }

    // <-- SỬA LẠI: Tách logic sửa ra một hàm riêng
    private void showEditExpenseDialog(Expense expense, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_expense, null);
        EditText etTitle = dialogView.findViewById(R.id.et_edit_title);
        EditText etAmount = dialogView.findViewById(R.id.et_edit_amount);
        EditText etDescription = dialogView.findViewById(R.id.et_edit_description);
        // LƯU Ý: Dialog này chưa có Spinner để đổi category, sẽ xử lý ở bước sau

        etTitle.setText(expense.getTitle());
        etAmount.setText(String.valueOf(expense.getAmount()));
        etDescription.setText(expense.getDescription());

        new AlertDialog.Builder(context)
                .setTitle("Chỉnh sửa chi tiêu")
                .setView(dialogView)
                .setPositiveButton("Cập nhật", (dialogInterface, which) -> {
                    String newTitle = etTitle.getText().toString().trim();
                    String newAmountStr = etAmount.getText().toString().trim();
                    String newDescription = etDescription.getText().toString().trim();

                    if (!newTitle.isEmpty() && !newAmountStr.isEmpty()) {
                        double newAmount = Double.parseDouble(newAmountStr);
                        // Gọi hàm cập nhật trên luồng nền
                        updateExpenseInBackground(expense, position, newTitle, newAmount, newDescription);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // <-- THÊM MỚI: Hàm cập nhật trên luồng nền
    private void updateExpenseInBackground(Expense oldExpense, int position, String newTitle, double newAmount, String newDescription) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);

            double oldAmount = oldExpense.getAmount();
            String budgetName = oldExpense.getCategory(); // Giả định không đổi category

            // 1. Tính toán số tiền chênh lệch
            double amountDifference = newAmount - oldAmount;

            // 2. Cập nhật budget tương ứng với số tiền chênh lệch
            // Nếu chi nhiều hơn -> cộng thêm, nếu chi ít hơn -> trừ bớt
            if (budgetName != null && !budgetName.isEmpty()) {
                db.budgetDao().updateSpentAmount(budgetName, amountDifference);
            }

            // 3. Cập nhật lại thông tin cho expense
            oldExpense.setTitle(newTitle);
            oldExpense.setAmount(newAmount);
            oldExpense.setDescription(newDescription);
            db.expenseDao().update(oldExpense);

            ((MainMenuActivity) context).runOnUiThread(() -> {
                notifyItemChanged(position);
                Toast.makeText(context, "Đã cập nhật chi tiêu", Toast.LENGTH_SHORT).show();
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
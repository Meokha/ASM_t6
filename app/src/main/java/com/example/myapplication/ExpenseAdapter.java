package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private List<Expense> expenseList;

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
        holder.tvAmount.setText("$" + expense.getAmount());
        holder.tvDescription.setText(expense.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(expense.getCreatedAt())); // Nếu dùng kiểu long
        holder.tvDate.setText(formattedDate);

        // Xử lý nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Expense")
                    .setMessage("Do you want to delete this expense?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        AppDatabase db = AppDatabase.getInstance(context);
                        db.expenseDao().delete(expenseList.get(position));

                        expenseList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, expenseList.size());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Xử lý nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_expense, null);
            EditText etTitle = dialogView.findViewById(R.id.et_edit_title);
            EditText etAmount = dialogView.findViewById(R.id.et_edit_amount);
            EditText etDescription = dialogView.findViewById(R.id.et_edit_description);

            etTitle.setText(expense.getTitle());
            etAmount.setText(String.valueOf(expense.getAmount()));
            etDescription.setText(expense.getDescription());

            new AlertDialog.Builder(context)
                    .setTitle("Edit Expense")
                    .setView(dialogView)
                    .setPositiveButton("Update", (dialogInterface, which) -> {
                        String newTitle = etTitle.getText().toString().trim();
                        String newAmount = etAmount.getText().toString().trim();
                        String newDescription = etDescription.getText().toString().trim();

                        if (!newTitle.isEmpty() && !newAmount.isEmpty()) {
                            expense.setTitle(newTitle);
                            expense.setAmount(Double.parseDouble(newAmount));
                            expense.setDescription(newDescription);

                            AppDatabase db = AppDatabase.getInstance(context);
                            db.expenseDao().update(expense);

                            notifyItemChanged(position);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAmount, tvDescription,  tvDate;
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

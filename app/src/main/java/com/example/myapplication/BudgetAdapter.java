package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.Budget;
import com.example.myapplication.budgets.CreateBudgetActivity;


import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private Context context;
    private List<Budget> budgetList;

    public BudgetAdapter(Context context, List<Budget> budgetList) {
        this.context = context;
        this.budgetList = budgetList;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        holder.txtCategory.setText(budget.getName());
        holder.txtAmount.setText(String.valueOf(budget.getAmount()));
        holder.txtDescription.setText(budget.getDescription());
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Budget")
                    .setMessage("Do you want to delete this budget?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        AppDatabase db = AppDatabase.getInstance(context);
                        db.budgetDao().delete(budgetList.get(position));

                        budgetList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, budgetList.size());
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateBudgetActivity.class);
            intent.putExtra("BUDGET_ID", budget.getId()); // Gửi ID để sửa
            context.startActivity(intent);
            // TODO: Thêm logic chỉnh sửa tại đây, ví dụ mở AddBudgetActivity kèm dữ liệu
        });
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateBudgetActivity.class);
            intent.putExtra("BUDGET_ID", budget.getId()); // Gửi ID để chỉnh sửa
            context.startActivity(intent);
        });

// Nút Delete
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Budget")
                    .setMessage("Do you want to delete this budget?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        AppDatabase db = AppDatabase.getInstance(context);
                        db.budgetDao().delete(budget);
                        budgetList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, budgetList.size());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtAmount, txtDescription;
        Button btnEdit, btnDelete;
        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.budgets.CreateBudgetActivity;
import com.example.myapplication.entity.Budget;

import java.text.NumberFormat;
import java.text.SimpleDateFormat; // <-- THÊM MỚI
import java.util.Date;          // <-- THÊM MỚI
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private Context context;
    private List<Budget> budgetList;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    // <-- THÊM MỚI: Định dạng ngày tháng
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


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

        // --- Hiển thị các thông tin cũ ---
        double totalAmount = budget.getAmount();
        double spentAmount = budget.getSpentAmount();
        double remainingAmount = totalAmount - spentAmount;

        holder.tvBudgetName.setText(budget.getName());
        holder.tvTotalAmount.setText(currencyFormatter.format(totalAmount));
        holder.tvSpentAmount.setText("Đã chi: " + currencyFormatter.format(spentAmount));
        holder.tvRemainingAmount.setText("Còn lại: " + currencyFormatter.format(remainingAmount));

        if (totalAmount > 0) {
            holder.pbBudgetProgress.setProgress((int) ((spentAmount / totalAmount) * 100));
        } else {
            holder.pbBudgetProgress.setProgress(0);
        }

        if (budget.getDescription() != null && !budget.getDescription().isEmpty()) {
            holder.txtDescription.setText(budget.getDescription());
            holder.txtDescription.setVisibility(View.VISIBLE);
        } else {
            holder.txtDescription.setVisibility(View.GONE);
        }

        // --- THÊM MỚI: Logic để hiển thị ngày của budget ---
        if (budget.getBudgetDate() > 0) { // Chỉ hiển thị nếu có ngày hợp lệ
            String dateString = dateFormatter.format(new Date(budget.getBudgetDate()));
            holder.tvItemBudgetDate.setText("Ngày: " + dateString);
            holder.tvItemBudgetDate.setVisibility(View.VISIBLE);
        } else {
            // Ẩn đi nếu budget này không có ngày (ví dụ: dữ liệu cũ)
            holder.tvItemBudgetDate.setVisibility(View.GONE);
        }
        // --- KẾT THÚC PHẦN THÊM MỚI ---


        // --- Logic các nút bấm (giữ nguyên) ---
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateBudgetActivity.class);
            intent.putExtra("BUDGET_ID", budget.getId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa ngân sách")
                    .setMessage("Bạn có chắc muốn xóa ngân sách này không?")
                    .setPositiveButton("Có", (dialog, which) -> deleteBudgetInBackground(budget, position))
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    private void deleteBudgetInBackground(Budget budget, int position) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase.getInstance(context).budgetDao().delete(budget);
            ((MainMenuActivity) context).runOnUiThread(() -> {
                budgetList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, budgetList.size());
            });
        });
    }

    @Override
    public int getItemCount() {
        return budgetList != null ? budgetList.size() : 0;
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        // Khai báo các view
        TextView tvBudgetName, tvTotalAmount, txtDescription, tvSpentAmount, tvRemainingAmount;
        TextView tvItemBudgetDate; // <-- THÊM MỚI
        ProgressBar pbBudgetProgress;
        Button btnEdit, btnDelete;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các view
            tvBudgetName = itemView.findViewById(R.id.tvBudgetName);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            tvSpentAmount = itemView.findViewById(R.id.tvSpentAmount);
            tvRemainingAmount = itemView.findViewById(R.id.tvRemainingAmount);
            pbBudgetProgress = itemView.findViewById(R.id.pbBudgetProgress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            // <-- THÊM MỚI: Ánh xạ TextView ngày
            tvItemBudgetDate = itemView.findViewById(R.id.tvItemBudgetDate);
        }
    }
}
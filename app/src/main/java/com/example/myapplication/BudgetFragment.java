package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.budgets.CreateBudgetActivity;
import com.example.myapplication.entity.Budget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService; // <-- THÊM MỚI
import java.util.concurrent.Executors;    // <-- THÊM MỚI

public class BudgetFragment extends Fragment {

    private RecyclerView recyclerViewBudget;
    private BudgetAdapter budgetAdapter;
    private List<Budget> budgetList = new ArrayList<>(); // <-- THÊM MỚI: Khởi tạo list ngay từ đầu
    private FloatingActionButton btnAddBudget;
    private AppDatabase db; // <-- THÊM MỚI: Tạo biến để lưu trữ instance của DB

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // <-- THÊM MỚI: Lấy instance của DB một lần và tái sử dụng
        db = AppDatabase.getInstance(getContext());

        // --- Cấu hình RecyclerView ---
        recyclerViewBudget = view.findViewById(R.id.rvBudget);
        // budgetList đã được khởi tạo ở trên
        budgetAdapter = new BudgetAdapter(getContext(), budgetList);
        recyclerViewBudget.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBudget.setAdapter(budgetAdapter);

        // --- Cấu hình các nút FloatingActionButton ---
        btnAddBudget = view.findViewById(R.id.btnAddBudget);
        btnAddBudget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateBudgetActivity.class);
            startActivity(intent);
        });

//        FloatingActionButton fabAddExpense = view.findViewById(R.id.fab_add_expense);
//        fabAddExpense.setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), AddExpenseActivity.class);
//            startActivity(intent);
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Gọi hàm loadBudgets mỗi khi Fragment được hiển thị lại
        loadBudgets();
    }

    private void loadBudgets() {
        // <-- THAY ĐỔI: Chạy việc truy vấn DB trên một luồng nền
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Lấy dữ liệu mới nhất từ DB (chạy ở luồng nền)
            List<Budget> newBudgets = db.budgetDao().getAllBudgets();

            // Sau khi có dữ liệu, quay lại UI Thread để cập nhật RecyclerView
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    budgetList.clear();
                    budgetList.addAll(newBudgets);
                    budgetAdapter.notifyDataSetChanged(); // Yêu cầu Adapter vẽ lại giao diện
                });
            }
        });
    }
}
package com.example.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.entity.Expense;

import java.util.List;

public class ExpensesFragment extends Fragment {

    private RecyclerView rvExpenses;
    private ExpenseAdapter expenseAdapter;

    public ExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvExpenses = view.findViewById(R.id.rvExpenses);
        rvExpenses.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load dữ liệu ban đầu
        List<Expense> expenses = AppDatabase.getInstance(getContext()).expenseDao().getAllExpenses();
        expenseAdapter = new ExpenseAdapter(getContext(), expenses);
        rvExpenses.setAdapter(expenseAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại danh sách mỗi lần quay lại
        List<Expense> updatedExpenses = AppDatabase.getInstance(getContext()).expenseDao().getAllExpenses();
        expenseAdapter.updateData(updatedExpenses);
    }
}

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.budgets.CreateBudgetActivity;
import com.example.myapplication.AppDatabase;
import com.example.myapplication.entity.Budget;
import com.example.myapplication.BudgetAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.myapplication.AddExpenseActivity;


import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    private RecyclerView recyclerViewBudget;
    private BudgetAdapter budgetAdapter;
    private List<Budget> budgetList;
    private FloatingActionButton btnAddBudget;


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

       // btnCreate = view.findViewById(R.id.btnCreateBudget);
        recyclerViewBudget = view.findViewById(R.id.rvBudget);

        budgetList = new ArrayList<>();
        budgetAdapter = new BudgetAdapter(getContext(), budgetList);

        recyclerViewBudget.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBudget.setAdapter(budgetAdapter);

        btnAddBudget = view.findViewById(R.id.btnAddBudget);
        btnAddBudget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateBudgetActivity.class);
            startActivity(intent);
        });
        FloatingActionButton fabAddExpense = view.findViewById(R.id.fab_add_expense);
        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddExpenseActivity.class); // hoặc AddExpenseActivity
            startActivity(intent);
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        loadBudgets();
    }



    private void loadBudgets() {
        List<Budget> newBudgets = AppDatabase.getInstance(getContext())
                .budgetDao()
                .getAllBudgets(); // hoặc getAll() tùy bạn đặt tên

        budgetList.clear();
        budgetList.addAll(newBudgets);
        budgetAdapter.notifyDataSetChanged();
    }


}

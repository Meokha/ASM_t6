package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.Budget;
import com.example.myapplication.entity.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView rvRecentExpenses;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private TextView tvRemainingBudget, tvBudgetUsage;


    private TextView tvTotalBudget, tvSpentAmount;
    private LinearProgressIndicator progressBar;
    private ActivityResultLauncher<Intent> addExpenseLauncher;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Đăng ký launcher để nhận kết quả từ AddExpenseActivity
        addExpenseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Nếu có kết quả trả về thành công
                        double amount = result.getData().getDoubleExtra("EXTRA_AMOUNT", 0);
                        String budgetName = result.getData().getStringExtra("EXTRA_BUDGET_NAME");

                        // Hiển thị thông báo tùy chỉnh
                        showCustomToast(amount, budgetName);

                        // onResume() sẽ tự động được gọi sau khi Activity đóng,
                        // nên nó sẽ tự cập nhật lại giao diện.
                    }
                }
        );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvRemainingBudget = view.findViewById(R.id.tv_remaining_budget);
        tvBudgetUsage = view.findViewById(R.id.tv_budget_usage);

        // Ánh xạ view tổng quan ngân sách
        tvTotalBudget = view.findViewById(R.id.tv_total_budget);
        tvSpentAmount = view.findViewById(R.id.tv_spent_amount);
        progressBar = view.findViewById(R.id.budget_progress);

        // RecyclerView hiển thị chi phí gần đây
        rvRecentExpenses = view.findViewById(R.id.rv_recent_expenses);
        rvRecentExpenses.setLayoutManager(new LinearLayoutManager(getContext()));

        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(getContext(), expenseList);
        rvRecentExpenses.setAdapter(expenseAdapter);

        // Nút thêm chi phí
        FloatingActionButton fabAddExpense = view.findViewById(R.id.btn_add_expense);
        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
            addExpenseLauncher.launch(intent);
        });
//        ImageButton btnReport = view.findViewById(R.id.btn_report);
//        btnReport.setOnClickListener(v -> {
//            Fragment reportFragment = new ReportFragment();
//            requireActivity()
//                    .getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main, reportFragment)
//                    .addToBackStack(null)
//                    .commit();
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecentExpenses();
        updateOverview(); // <== THÊM GỌI LẠI UPDATE OVERVIEW
    }
    private void showCustomToast(double amount, String budgetName) {
        if (getContext() == null) return;

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) getView().findViewById(R.id.custom_toast_container));

        TextView tvMessage = layout.findViewById(R.id.tv_toast_message);

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String message = "Spent -" + currencyFormatter.format(amount) + " from " + budgetName;
        tvMessage.setText(message);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 50); // Vị trí ở trên cùng
        toast.setDuration(Toast.LENGTH_LONG); // Thời gian hiển thị
        toast.setView(layout);
        toast.show();
    }
    private void loadRecentExpenses() {
        List<Expense> allExpenses = AppDatabase.getInstance(getContext())
                .expenseDao()
                .getAllExpenses();

        // Lấy 3 khoản gần nhất (nếu đủ)
        List<Expense> recent3 = allExpenses.size() > 2 ?
                allExpenses.subList(0, 3) :
                allExpenses;

        expenseList.clear();
        expenseList.addAll(recent3);
        expenseAdapter.notifyDataSetChanged();
    }

    private void updateOverview() {
        AppDatabase db = AppDatabase.getInstance(getContext());

        // Tính tổng ngân sách
        List<Budget> budgets = db.budgetDao().getAllBudgets();
        double totalBudget = 0;
        for (Budget b : budgets) {
            totalBudget += b.getAmount();
        }

        // Tính tổng chi tiêu
        List<Expense> expenses = db.expenseDao().getAllExpenses();
        double totalSpent = 0;
        for (Expense e : expenses) {
            totalSpent += e.getAmount();
        }

        // Tính còn lại và phần trăm
        double remaining = totalBudget - totalSpent;
        int usagePercent = totalBudget > 0 ? (int) ((totalSpent / totalBudget) * 100) : 0;

        // Cập nhật UI
        tvTotalBudget.setText("$" + String.format("%.2f", totalBudget));
        tvSpentAmount.setText("$" + String.format("%.2f", totalSpent));
        tvRemainingBudget.setText("$" + String.format("%.2f", remaining));
        tvBudgetUsage.setText(usagePercent + "%");

        progressBar.setProgress(usagePercent);
    }

}

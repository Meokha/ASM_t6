package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.entity.Expense;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {

    private MaterialButton btnStartDate, btnEndDate, btnGenerate;
    private TextView tvReportPeriod, tvTotalExpense;
    private PieChart pieChart;
    private BarChart barChart;

    private Date startDate, endDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ReportFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind UI
        btnStartDate = view.findViewById(R.id.btn_start_date);
        btnEndDate = view.findViewById(R.id.btn_end_date);
        btnGenerate = view.findViewById(R.id.btn_generate_report);
        tvReportPeriod = view.findViewById(R.id.tv_report_period);
        tvTotalExpense = view.findViewById(R.id.tv_total_expense);
        pieChart = view.findViewById(R.id.pie_chart_category);
        barChart = view.findViewById(R.id.bar_chart_daily);

        // Khởi tạo ngày mặc định là hôm nay
        startDate = endDate = new Date();
        updateDateTexts();

        btnStartDate.setOnClickListener(v -> showDatePicker(true));
        btnEndDate.setOnClickListener(v -> showDatePicker(false));

        btnGenerate.setOnClickListener(v -> generateReport());
    }

    private void showDatePicker(boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        if (isStartDate) calendar.setTime(startDate);
        else calendar.setTime(endDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    if (isStartDate) startDate = calendar.getTime();
                    else endDate = calendar.getTime();

                    updateDateTexts();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateDateTexts() {
        String text = "Report from " + dateFormat.format(startDate) + " to " + dateFormat.format(endDate);
        tvReportPeriod.setText(text);
    }

    private void generateReport() {
        List<Expense> allExpenses = AppDatabase.getInstance(requireContext()).expenseDao().getAllExpenses();

        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense e : allExpenses) {
            Date date = e.getDate(); // make sure this returns java.util.Date
            if (date != null && !date.before(startDate) && !date.after(endDate)) {
                filteredExpenses.add(e);
            }
        }

        if (filteredExpenses.isEmpty()) {
            Toast.makeText(getContext(), "No expenses found in selected range", Toast.LENGTH_SHORT).show();
        }

        updateTotalExpense(filteredExpenses);
        setupPieChart(filteredExpenses);
        setupBarChart(filteredExpenses);
    }

    private void updateTotalExpense(List<Expense> expenses) {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        tvTotalExpense.setText("$" + String.format("%.2f", total));
    }

    private void setupPieChart(List<Expense> expenses) {
        HashMap<String, Double> categoryMap = new HashMap<>();
        for (Expense e : expenses) {
            String category = e.getCategory(); // You need to have a category field in Expense
            double amount = e.getAmount();
            categoryMap.put(category, categoryMap.getOrDefault(category, 0.0) + amount);
        }

        List<PieEntry> entries = new ArrayList<>();
        for (String category : categoryMap.keySet()) {
            entries.add(new PieEntry(categoryMap.get(category).floatValue(), category));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(new int[]{
                R.color.purple_200, R.color.teal_200, R.color.purple_500,
                R.color.primary_light, R.color.black
        }, getContext());

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(12f);
        pieChart.setData(pieData);

        Description desc = new Description();
        desc.setText("By Category");
        pieChart.setDescription(desc);
        pieChart.invalidate();
    }

    private void setupBarChart(List<Expense> expenses) {
        HashMap<String, Double> dayMap = new HashMap<>();
        for (Expense e : expenses) {
            String day = dateFormat.format(e.getDate());
            double amount = e.getAmount();
            dayMap.put(day, dayMap.getOrDefault(day, 0.0) + amount);
        }

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int index = 0;
        for (String day : dayMap.keySet()) {
            entries.add(new BarEntry(index, dayMap.get(day).floatValue()));
            labels.add(day);
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Daily Expenses");
        dataSet.setColor(getResources().getColor(R.color.teal_700));
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.setFitBars(true);
        Description desc = new Description();
        desc.setText("By Day");
        barChart.setDescription(desc);
        barChart.invalidate();
    }
}

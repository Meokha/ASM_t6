package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.dao.BudgetDao;
import com.example.myapplication.dao.ExpenseDao;
import com.example.myapplication.entity.Budget;
import com.example.myapplication.entity.Expense;


@Database(entities = {Budget.class, Expense.class}, version = 3, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract BudgetDao budgetDao();
    public abstract ExpenseDao expenseDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "expense-db"
                            )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration() // ✅ THÊM DÒNG NÀY
// CHỈ dùng để demo/test, tránh dùng thật
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

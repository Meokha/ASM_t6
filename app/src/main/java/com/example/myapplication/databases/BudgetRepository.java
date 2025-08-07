package com.example.myapplication.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BudgetRepository extends Dbhelpers{
    public BudgetRepository(@Nullable Context context) {
        super(context);
    }
    // tao moi budget luu vao database
    public long addNewBudget(String nameBudget, int money, String description){
        // lay ra ngay thang hien tai
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        @SuppressLint({"NewApi", "LocalSuppress"}) ZonedDateTime zoneDt = ZonedDateTime.now();
        @SuppressLint({"NewApi", "LocalSuppress"}) String currentDate = dtf.format(zoneDt);
        ContentValues values = new ContentValues();
        values.put(Dbhelpers.COL_BUDGET_NAME, nameBudget);
        values.put(Dbhelpers.COL_BUGET_MONEY, money);
        values.put(Dbhelpers.COL_BUDGET_DESCRIPTION, description );
        values.put(Dbhelpers.COL_BUDGET_STATUS, 1);
        values.put(Dbhelpers.COL_CREATED_AT, currentDate);
        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(Dbhelpers.TABLE_BUDGET, null, values);
        db.close();
        return insert;
    }
}

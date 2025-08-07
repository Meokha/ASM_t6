package com.example.myapplication.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.DatePicker;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Dbhelpers extends SQLiteOpenHelper {
    // day se la noi dinh nghia va tao ra csdl va cac bang du lieu
    // logCat Tag
    private static final String LOG = Dbhelpers.class.getName(); // lay ten class luon
    // tao ten cho csdl
    private static final String DB_NAME = "campus_expenses";
    private static  final int DB_VERSION = 2;
    // tao ten bang csdl
    protected static final String TABLE_USERS = "users";
    // ten cac cot trong bang csdl
    protected static final String COL_ID = "id";
    protected static final String COL_USERNAME = "username";
    protected static final String COL_PASSWORD = "password";
    protected static final String COL_EMAIL = "email";
    protected static final String COL_PHONE = "phone";
    protected static final String COL_ROLE = "role";
    protected static final String COL_CREATED_AT = "created_at";
    protected static final String COL_UPDATED_AT = "updated_at";
    // tao bang du lieu

    private static final String CREATE_TABLE_USERS = " CREATE TABLE " +
            TABLE_USERS + " ( " +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USERNAME + " VARCHAR(30) NOT NULL, " +
            COL_PASSWORD + " VARCHAR(200) NOT NULL, " +
            COL_EMAIL + " VARCHAR(60) NOT NULL, " +
            COL_PHONE + " VARCHAR(20), " +
            COL_ROLE + " INTEGER NOT NULL, " +
            COL_CREATED_AT + " DATETIME, " +
            COL_UPDATED_AT + " DATETIME ) ";

    //dinh nghia bang budget
    protected static final String TABLE_BUDGET = "budget";
    protected static final String COL_BUDGET_ID = "id";
    protected static final String COL_BUDGET_NAME = "name";
    protected static final String COL_BUGET_MONEY = "money";
    protected static final String COL_BUDGET_DESCRIPTION = "description";
    protected static final String COL_BUDGET_STATUS ="status_budget";

    private final String CREATE_TABLE_BUDGET = " CREATE TABLE " +
            TABLE_BUDGET + " ( " +
            COL_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_BUDGET_NAME + " VARCHAR(100) NOT NULL, " +
            COL_BUGET_MONEY + " INTEGER NOT NULL, " +
            COL_BUDGET_DESCRIPTION + " TEXT, " +
            COL_BUDGET_STATUS + " TINYINT NOT NULL, " +
            COL_CREATED_AT + " DATETIME, " +
            COL_UPDATED_AT + " DATETIME ) ";

    public Dbhelpers(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BUDGET);
        // tao them cac bang khac
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
            onCreate(db);
        }
    }
}

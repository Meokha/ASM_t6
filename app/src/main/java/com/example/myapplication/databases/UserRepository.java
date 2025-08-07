package com.example.myapplication.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserRepository extends Dbhelpers {
    public UserRepository(@Nullable Context context) {
        super(context);
    }
    // viet ham insert account user
    @SuppressLint("NewApi")
    public long saveUserAccount(
            String username,
            String password,
            String email,
            String phone
    ) {
        // lay ra ngay thang hien tai
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        @SuppressLint({"NewApi", "LocalSuppress"}) ZonedDateTime zoneDt = ZonedDateTime.now();
        @SuppressLint({"NewApi", "LocalSuppress"}) String currentDate = dtf.format(zoneDt);

        ContentValues values = new ContentValues();
        values.put(Dbhelpers.COL_USERNAME, username );// save data into COL_USERNAME
        values.put(Dbhelpers.COL_PASSWORD, password);
        values.put(Dbhelpers.COL_EMAIL, email);
        values.put(Dbhelpers.COL_PHONE, phone);
        values.put(Dbhelpers.COL_ROLE,0);
        values.put(Dbhelpers.COL_CREATED_AT, currentDate);
        SQLiteDatabase db = this.getWritableDatabase() ;
        long insert = db.insert(Dbhelpers.TABLE_USERS, null, values);
        db.close();
        return insert;
    }


    @SuppressLint("Range")
    public UserModel getInfoAccountByUsername(String username, String password){

        UserModel userAccount = new UserModel();

        try{
            SQLiteDatabase db = this.getReadableDatabase(); // cau lenh select
            // tao mang chua cac cot du lieu muon thao tac trong bang du lieu
            // select id, username, email, phone, role from user where username = ? and password = ?

            String[] cols = {Dbhelpers.COL_ID, Dbhelpers.COL_USERNAME, Dbhelpers.COL_EMAIL, Dbhelpers.COL_PHONE, Dbhelpers.COL_ROLE   };

            String condition = Dbhelpers.COL_USERNAME + " =? AND " + Dbhelpers.COL_PASSWORD + " =? ";
            String[] params = { username, password};
            Cursor data = db.query(Dbhelpers.TABLE_USERS, cols, condition, params, null, null, null) ;

            if (data.getCount() > 0){
                // co du lieu trong bang
                data.moveToFirst();// chi lay ra 1 dong du lieu( vi username khong trung nhau)
                // do du lieu vao  user     model
                userAccount.setId(data.getInt(data.getColumnIndex(Dbhelpers.COL_ID)));
                userAccount.setUsername(data.getString(data.getColumnIndex(Dbhelpers.COL_USERNAME)));
                userAccount.setEmail(data.getString(data.getColumnIndex(Dbhelpers.COL_EMAIL)));
                userAccount.setPhone(data.getString(data.getColumnIndex(Dbhelpers.COL_PHONE)));
                userAccount.setRole(data.getInt(data.getColumnIndex(Dbhelpers.COL_ROLE)));
            }
            data.close(); // khong truy van nua

            db.close();// ngat ket noi csdl
        }
        catch (RuntimeException e){
            throw new RuntimeException(e);
        }

        return userAccount;
    }
}
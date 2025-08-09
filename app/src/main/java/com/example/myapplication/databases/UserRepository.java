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

    /**
     * Hàm đăng ký: Lưu một tài khoản người dùng mới vào database.
     */
    @SuppressLint("NewApi")
    public long saveUserAccount(String username, String password, String email, String phone) {
        // Lấy ngày giờ hiện tại
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zoneDt = ZonedDateTime.now();
        String currentDate = dtf.format(zoneDt);

        // Chuẩn bị dữ liệu để chèn vào
        ContentValues values = new ContentValues();
        values.put(Dbhelpers.COL_USERNAME, username);
        values.put(Dbhelpers.COL_PASSWORD, password);
        values.put(Dbhelpers.COL_EMAIL, email);
        values.put(Dbhelpers.COL_PHONE, phone);
        values.put(Dbhelpers.COL_ROLE, 0); // Mặc định role là 0
        values.put(Dbhelpers.COL_CREATED_AT, currentDate);

        // Mở kết nối và chèn dữ liệu
        SQLiteDatabase db = this.getWritableDatabase();
        long insertResult = db.insert(Dbhelpers.TABLE_USERS, null, values);
        db.close();

        // Trả về ID của dòng mới được chèn, hoặc -1 nếu có lỗi
        return insertResult;
    }

    /**
     * Hàm đăng nhập: Kiểm tra username và password.
     * Trả về UserModel nếu đúng, trả về null nếu sai.
     */
    @SuppressLint("Range")
    public UserModel getInfoAccountByUsername(String username, String password) {
        UserModel userAccount = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] cols = {
                Dbhelpers.COL_ID,
                Dbhelpers.COL_USERNAME,
                Dbhelpers.COL_PASSWORD,
                Dbhelpers.COL_EMAIL,
                Dbhelpers.COL_PHONE,
                Dbhelpers.COL_ROLE
        };
        String condition = Dbhelpers.COL_USERNAME + " = ? AND " + Dbhelpers.COL_PASSWORD + " = ?";
        String[] params = { username, password };

        Cursor data = db.query(Dbhelpers.TABLE_USERS, cols, condition, params, null, null, null);

        if (data != null && data.moveToFirst()) {
            userAccount = new UserModel();
            userAccount.setId(data.getInt(data.getColumnIndex(Dbhelpers.COL_ID)));
            userAccount.setUsername(data.getString(data.getColumnIndex(Dbhelpers.COL_USERNAME)));
            userAccount.setPassword(data.getString(data.getColumnIndex(Dbhelpers.COL_PASSWORD)));
            userAccount.setEmail(data.getString(data.getColumnIndex(Dbhelpers.COL_EMAIL)));
            userAccount.setPhone(data.getString(data.getColumnIndex(Dbhelpers.COL_PHONE)));
            userAccount.setRole(data.getInt(data.getColumnIndex(Dbhelpers.COL_ROLE)));
        }

        if (data != null) {
            data.close();
        }
        db.close();
        return userAccount;
    }

    /**
     * Hàm cho trang Settings: Lấy thông tin chi tiết chỉ bằng username.
     */
    @SuppressLint("Range")
    public UserModel getInfoAccountByUsername(String username) {
        UserModel userAccount = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] cols = {
                Dbhelpers.COL_ID,
                Dbhelpers.COL_USERNAME,
                Dbhelpers.COL_PASSWORD,
                Dbhelpers.COL_EMAIL,
                Dbhelpers.COL_PHONE,
                Dbhelpers.COL_ROLE
        };
        String selection = Dbhelpers.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(Dbhelpers.TABLE_USERS, cols, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            userAccount = new UserModel();
            userAccount.setId(cursor.getInt(cursor.getColumnIndex(Dbhelpers.COL_ID)));
            userAccount.setUsername(cursor.getString(cursor.getColumnIndex(Dbhelpers.COL_USERNAME)));
            userAccount.setPassword(cursor.getString(cursor.getColumnIndex(Dbhelpers.COL_PASSWORD)));
            userAccount.setEmail(cursor.getString(cursor.getColumnIndex(Dbhelpers.COL_EMAIL)));
            userAccount.setPhone(cursor.getString(cursor.getColumnIndex(Dbhelpers.COL_PHONE)));
            userAccount.setRole(cursor.getInt(cursor.getColumnIndex(Dbhelpers.COL_ROLE)));
            cursor.close();
        }
        db.close();
        return userAccount;
    }

    /**
     * Hàm cho trang Settings: Cập nhật mật khẩu mới.
     */
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Dbhelpers.COL_PASSWORD, newPassword);
        String selection = Dbhelpers.COL_USERNAME + " = ?";
        String[] selectionArgs = { username };
        int count = db.update(Dbhelpers.TABLE_USERS, values, selection, selectionArgs);
        db.close();
        return count > 0;
    }
}
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DemoComponentActivity extends AppCompatActivity {
    Button btnExitApp;
    EditText edtDate;
    TextView tvId, tvPhone, tvUrl;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_component);
        // anh xa phan tu ngoai view
        btnExitApp = findViewById(R.id.btnExitApp);
        edtDate = findViewById(R.id.edtTime);
        tvId = findViewById(R.id.tvUserId);
        tvUrl = findViewById(R.id.tvURL);
        tvPhone = findViewById(R.id.tvPhone);
        // get data from DemoIntent Activity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            String url = bundle.getString("MY_URL", "");
            String phone = bundle.getString("MY_PHONE", "");
            int idUser = bundle.getInt("ID_USER", 0);
            tvId.setText("UserID : " + String.valueOf(idUser));
            tvUrl.setText("URL: " + url);
            tvPhone.setText("Phone: " + phone);
        }

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR); // nam hien tai
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                // tao Date picker dialog
                DatePickerDialog datePicker = new DatePickerDialog(
                        DemoComponentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                                edtDate.setText(date);
                            }
                        }, year, month, day);
                datePicker.setCancelable(false);
                datePicker.show();
            }
        });

        // bat su kien
        btnExitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tao mot hop thoai thong bao (Alert Dialog)
                AlertDialog.Builder builder = new AlertDialog.Builder(DemoComponentActivity.this);
                builder.setMessage("Do you want to exit Application ?");
                builder.setTitle("Alert !!!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",(DialogInterface.OnClickListener) (dialog, which) -> {
                   // button yes - thoat app
                   finish(); // dong app
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                   dialog.cancel(); // khong lam gi ca
                    // Button No
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}

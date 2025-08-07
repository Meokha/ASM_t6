package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    private static final String LOG_ACTIVITY = "logActivity";
    Button btnFirstActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ham nay se la noi khai bao cac bien hay load cac layout giao dien
        // ham nay se chay ngay khi co 1 activity duoc kich hoat
        setContentView(R.layout.activity_test);
        Log.i(LOG_ACTIVITY, "******* onCreate *********");
        // anh xa View: tim phan tu ngoai view thong qua ID cua no
        btnFirstActivity = findViewById(R.id.btnFirstActivity);
        // bat su kien cho Button - khi nguoi dung click vao no
        btnFirstActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bat ra mot thong bao
                Toast.makeText(TestActivity.this, "Click me - Hi you", Toast.LENGTH_SHORT).show();
                // di sang activity khac
                Intent intent = new Intent(TestActivity.this, TestSecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ham nay se dc goi truoc khi Activity hien thi tren man hinh
        Log.i(LOG_ACTIVITY, "****** onStart ********");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ham nay se duoc goi ngay sau khi Activity co the tuong tac voi nguoi dung
        Log.i(LOG_ACTIVITY, "******** onResume *********");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ham nay se duoc goi khi co Activity moi chuan bi duoc kich hoat
        Log.i(LOG_ACTIVITY, "******** onPause ********");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ham nay se duoc goi khi mot Activity bi an di, nhuong cho cho Activity khac hien thi
        Log.i(LOG_ACTIVITY, "********* onStop ***********");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // ham nay se chay khi mot Activity da tung bi an di hien thi quay tro lai
        // va keo theo ca ham onStart va onResume chay lai
        Log.i(LOG_ACTIVITY, "************* onRestart *************");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ham nay se duoc goi khi ung dung bi nguoi dung huy, giai phong bo nho cho thiet bi
        Log.i(LOG_ACTIVITY, "************ onDestroy ************");
    }
}

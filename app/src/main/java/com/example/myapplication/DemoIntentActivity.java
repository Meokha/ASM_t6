package com.example.myapplication;

import static android.Manifest.permission.CALL_PHONE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DemoIntentActivity extends AppCompatActivity {
    EditText edtUrl, edtPhone;
    Button btnUrl, btnPhone, btnGotoActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_intent);
        edtUrl = findViewById(R.id.edtURL);
        btnUrl = findViewById(R.id.btnLoadURL);
        edtPhone = findViewById(R.id.edtPhoneNumber);
        btnPhone = findViewById(R.id.btnCallPhone);
        btnGotoActivity = findViewById(R.id.btnGotoActivity);

        btnGotoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = edtUrl.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                // chuyen sang man hinh Activity khac, dong thoi gui du lieu sang man hinh Activity khac
                Intent intent = new Intent(DemoIntentActivity.this, DemoComponentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("MY_URL",url);
                bundle.putString("MY_PHONE", phone);
                bundle.putInt("ID_USER", 101);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    edtPhone.setError("Phone number can not empty");
                    return;
                }
                // call to phone number
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phone));
                // check xem thiet bi di dong co duoc call phone hay ko?
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    startActivity(intent);
                } else{
                    // xin quyen call phone
                    requestPermissions(new String[]{ CALL_PHONE }, 1);
                }
            }
        });

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lay dia chi URL nguoi dung nhap vao
                String url = edtUrl.getText().toString().trim();
                if (TextUtils.isEmpty(url)){
                    edtUrl.setError("URL not empty");
                    return;
                }
                // load noi dung website tu URL cua trang website do
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}

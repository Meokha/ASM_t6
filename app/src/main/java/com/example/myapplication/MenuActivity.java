package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    TextView tvAccount;
    Button btnLogout;
    Intent intent;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvAccount = findViewById(R.id.tvAccount);
        btnLogout = findViewById(R.id.btnLogout);
        // lay du lieu tu login truyen sang
        intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            String username = bundle.getString("USER_ACCOUNT", "");
            int idUser = bundle.getInt("ID_ACCOUNT", 0);
            String emailUser = bundle.getString("EMAIL_ACCOUNT", "");
            tvAccount.setText(username); // hien ten dang nhap
        }else {
            Intent login = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(login);
            finish(); // khong cho back tro lai
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove data ma Login ban sang
                if(bundle != null){
                    intent.removeExtra("USERNAME_ACCOUNT");
                    intent.removeExtra("ID_ACCOUNT");
                    intent.removeExtra("EMAIL_ACCOUNT");
                    intent.removeExtra("ROLE_ACCOUNT");
                }
                // quay lai trang login
                Intent login = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(login);
                finish(); // khong cho back tro lai
            }
        });
    }
}

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databases.UserModel;
import com.example.myapplication.databases.UserRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    TextView tvRegister;
    EditText edtUsername, edtPassword;
    Button btnLogin;
    UserRepository repository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        repository = new UserRepository(LoginActivity.this);
        tvRegister = findViewById(R.id.tvRegister);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        checkLoginUser(); // xu ly dang nhap
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    private void checkLoginWithDataFile(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    edtUsername.setError("Enter username, please !");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Enter password, please !");
                    return;
                }
                // read data file from internal storage (file user.txt)
                try {
                    FileInputStream fileInput = openFileInput("user.txt"); //mo file
                    int read = -1;
                    StringBuilder builder = new StringBuilder();
                    while ((read = fileInput.read()) != -1){
                        builder.append((char) read);
                        // lay toan bo du lieu tu trong file gan vao builder
                    }
                    fileInput.close(); // dong file
                    String[] userAccounts = builder.toString().trim().split("\n");
                    boolean checkLogin = false;
                    for (int i = 0; i < userAccounts.length; i++){
                        String user = userAccounts[i].substring(0, userAccounts[i].indexOf("|"));
                        String pass = userAccounts[i].substring(userAccounts[i].indexOf("|")+1);
                        if (username.equals(user) && password.equals(pass)){
                            checkLogin = true;
                            break;
                        }
                    }
                    if (checkLogin){
                        // login Success
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        // chuye vao Menu Activity
                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        // ban du lieu sang menu activity
                        startActivity(intent);
                    } else {
                        // login Fail
                        Toast.makeText(LoginActivity.this, "Account invalid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (FileNotFoundException e){
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void checkLoginUser(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    edtUsername.setError("Enter username, please!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Enter password, please!");
                    return;
                }
                // kiem tra tai khoan nguoi dung co ton tai trong database khong?
                UserModel infoAccount = repository.getInfoAccountByUsername(username, password);
                assert infoAccount != null;
                if (infoAccount.getUsername() != null&& infoAccount.getId() > 0){
                    // dang nhap thanh cong
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID_ACCOUNT", infoAccount.getId());
                    bundle.putString("USER_ACCOUNT", infoAccount.getUsername());
                    bundle.putString("EMAIL_ACCOUNT", infoAccount.getEmail());
                    bundle.putInt("ROlE_ACCOUNT", infoAccount.getRole());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();// khong back lai ve trang dang nhap nua

                } else {
                    // dang nhap that bai
                    Toast.makeText(LoginActivity.this, "Account invalid", Toast.LENGTH_LONG).show();
                }

            }
    });
    }
}

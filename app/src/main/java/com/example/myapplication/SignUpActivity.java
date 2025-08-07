package com.example.myapplication;

import android.content.Context;
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

import com.example.myapplication.databases.UserRepository;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class SignUpActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtEmail, edtPhone;
    Button btnRegister;
    TextView tvLogin;
    UserRepository repository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        repository  = new UserRepository(SignUpActivity.this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin  = findViewById(R.id.tvLogin);
        edtEmail = findViewById(R.id.edtMail);
        edtPhone = findViewById(R.id.edtPhone);
        signUpAccount();
    }
    private void signUpAccount(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    edtUsername.setError("Enter username, please");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Enter password, please");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    edtEmail.setError("Enter Email, please");
                    return;
                }
                // save account user to database
                long insert = repository.saveUserAccount(username, password, email, phone);
                if (insert == -1){
                    // Fail
                    Toast.makeText(SignUpActivity.this, "Sign up Fail", Toast.LENGTH_SHORT).show();
                } else {
                    // Success
                    Toast.makeText(SignUpActivity.this, "Sign up success", Toast.LENGTH_SHORT).show();
                    // quay ve trang dang nhap
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void signUpV1(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    edtUsername.setError("Username can not empty");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Password can not empty");
                    return;
                }
                // save data user to file
                FileOutputStream fileOutput = null;
                try {
                    username = username + "|";
                    fileOutput = openFileOutput("user.txt", Context.MODE_APPEND);
                    fileOutput.write(username.getBytes(StandardCharsets.UTF_8));
                    fileOutput.write(password.getBytes(StandardCharsets.UTF_8));
                    fileOutput.write('\n');
                    fileOutput.close(); // dong file
                    edtUsername.setText("");
                    edtPassword.setText("");
                    Toast.makeText(SignUpActivity.this, "SignUp account successfully", Toast.LENGTH_SHORT).show();
                    // quay ve trang dang nhap
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

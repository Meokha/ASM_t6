package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SharedPreferenceActivity extends AppCompatActivity {
    EditText edtNumber1, edtNumber2, edtResult;
    Button btnSum, btnClear;
    TextView tvHistory;
    String history = ""; // du lieu de luu vao Share Preference.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_preference);
        edtNumber1 = findViewById(R.id.edtNumber1);
        edtNumber2 = findViewById(R.id.edtNumber2);
        btnSum = findViewById(R.id.btnSumNumber);
        btnClear = findViewById(R.id.btnClearData);
        tvHistory = findViewById(R.id.tvHistory);
        edtResult = findViewById(R.id.edtResult);
        edtResult.setEnabled(false);

        // get data from Share Preferences
        SharedPreferences share = getSharedPreferences("CalculatorPlus", MODE_PRIVATE);
        history = share.getString("OPERATOR_PLUS", "");
        tvHistory.setText(history);

        btnSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long number1 = Integer.parseInt(edtNumber1.getText().toString().trim());
                long number2 = Integer.parseInt(edtNumber2.getText().toString().trim());
                long result = number1 + number2;
                edtResult.setText(String.valueOf(result));
                history += number1 + " + " + number2 + " = " + result;
                tvHistory.setText(history);
                history += "\n";
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history = "";
                tvHistory.setText("");
                edtNumber1.setText("");
                edtNumber2.setText("");
                edtResult.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // luu du lieu vao share Preferences
        SharedPreferences myPrf = getSharedPreferences("CalculatorPlus", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrf.edit();
        editor.putString("OPERATOR_PLUS", history);
        editor.apply();
    }
}

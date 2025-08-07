package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestEventActivity extends AppCompatActivity {
    private EditText edtData;
    private Button btnClickMe, btnClear;
    private CheckBox cbBlock;
    private TextView tvTitle;
    private RadioGroup radAddress;
    private RadioButton radHN, radHY, radPT, radTB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_event);
        // anh xa view
        edtData = findViewById(R.id.edtData);
        btnClickMe = findViewById(R.id.btnClickMe);
        btnClear = findViewById(R.id.btnClearData);
        cbBlock = findViewById(R.id.cbBlock);
        tvTitle = findViewById(R.id.tvTitle);
        radAddress = findViewById(R.id.radAddress);
        radHN = findViewById(R.id.radHaNoi);
        radHY = findViewById(R.id.radHY);
        radPT = findViewById(R.id.radPT);
        radTB = findViewById(R.id.radTB);

        // block
        edtData.setEnabled(false);
        btnClickMe.setEnabled(false);
        btnClear.setEnabled(false);

        edtData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim(); // lay noi dung nhap vao Edit Text
                tvTitle.setText(content);
            }
        });

        cbBlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edtData.setEnabled(true);
                    btnClickMe.setEnabled(true);
                    btnClear.setEnabled(true);
                } else {
                    edtData.setEnabled(false);
                    btnClickMe.setEnabled(false);
                    btnClear.setEnabled(false);
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtData.setText(" ");
            }
        });

        // bat su kien
        btnClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lay du lieu ma nguoi dung nhap vao Edit Text
//                String data = edtData.getText().toString().trim();
//                if (TextUtils.isEmpty(data)){
//                    edtData.setError("Can not empty, please");
//                    return; // dung chuong trinh
//                }
//                Toast.makeText(TestEventActivity.this, data, Toast.LENGTH_LONG).show();

                // xu ly xem nguoi dung chon Que o dau ?
                int selectedId = radAddress.getCheckedRadioButtonId();
                RadioButton radButton = (RadioButton) findViewById(selectedId);
                String address = radButton.getText().toString().trim();
                Toast.makeText(TestEventActivity.this, address, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

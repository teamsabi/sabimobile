package com.example.ngikngik;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class masukkanOTP extends AppCompatActivity {
    private Button btnbatal, lanjut;
    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView kirimulang;
    private ProgressBar progressBar;
    private int resendTime = 60;
    private boolean resendEnabled = false;
    private int selectedEtPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masukkan_otp);

        btnbatal = findViewById(R.id.batalotp);
        lanjut = findViewById(R.id.lanjut);
        progressBar = findViewById(R.id.progressBar2);
        kirimulang = findViewById(R.id.kirimulang);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);

        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);
        otp5.addTextChangedListener(textWatcher);
        otp6.addTextChangedListener(textWatcher);

        showKeyboard(otp1);

        kirimulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (resendEnabled) {
                    startCountDownTimer();
//                }
            }
        });

        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getOtp = otp1.getText().toString() + otp2.getText().toString() +
                        otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();

                if (getOtp.length() == 6) {
                    // Handle OTP verification here
                }
            }
        });
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                switch (selectedEtPosition) {
                    case 0: selectedEtPosition = 1; showKeyboard(otp2); break;
                    case 1: selectedEtPosition = 2; showKeyboard(otp3); break;
                    case 2: selectedEtPosition = 3; showKeyboard(otp4); break;
                    case 3: selectedEtPosition = 4; showKeyboard(otp5); break;
                    case 4: selectedEtPosition = 5; showKeyboard(otp6); break;
                    case 5: lanjut.setBackgroundResource(R.drawable.colorlanjut); break;
                }
            }
        }
    };

    private void showKeyboard(EditText otp) {
        otp.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otp, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer() {
        resendEnabled = false;
        kirimulang.setEnabled(false);
        kirimulang.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                kirimulang.setText("Kirim Ulang (" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true; // Pastikan resendEnabled diatur menjadi true setelah timer selesai
                kirimulang.setEnabled(true);
                kirimulang.setText("Kirim Ulang");
                kirimulang.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            }
        }.start();
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (selectedEtPosition > 0) {
                selectedEtPosition--;
                switch (selectedEtPosition) {
                    case 0: showKeyboard(otp1); break;
                    case 1: showKeyboard(otp2); break;
                    case 2: showKeyboard(otp3); break;
                    case 3: showKeyboard(otp4); break;
                    case 4: showKeyboard(otp5); break;
                }
                lanjut.setBackgroundResource(R.drawable.colorlanjut);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
package com.example.ngikngik;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class masukkanOTP extends Dialog {
    Button btnbatal;
    private EditText otp1,otp2,otp3,otp4,otp5,otp6;
    private TextView kirimulang;
    private Button lanjut;

    //resend otp time is seconds
    private int resendTime = 60;

    //will be true after 60 seconds
    private boolean resendEnabled = false;

    private int selectedEtPosition;
    public masukkanOTP(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masukkan_otp);
        Button btnbatal = findViewById(R.id.batalotp);
        Button buttonlanjut = findViewById(R.id.lanjut);
        ProgressBar progressBar = findViewById(R.id.progressBar2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_masukkan_otp);


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

        //By default open Keyboard on first EditText
        showKeyboard(otp1);

        //start countdown timer
        kirimulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resendEnabled){

                    //resend code here
                    starCountDownTimer();
                }
            }
        });
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String getOtp = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
                //handle verif process here
                if(getOtp.length() == 4){

                }
            }
        });
    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length() > 0) {
                 if(selectedEtPosition == 0){
                     //select next edit text
                     selectedEtPosition = 1;
                     showKeyboard(otp2);

                 } else if (selectedEtPosition == 1) {
                     //select next edit text
                     selectedEtPosition = 2;
                     showKeyboard(otp3);

                 } else if (selectedEtPosition == 2) {
                     //select next edit text
                     selectedEtPosition = 3;
                     showKeyboard(otp4);

                 } else if (selectedEtPosition == 2) {
                     //select next edit text
                     selectedEtPosition = 4;
                     showKeyboard(otp5);

                 } else if (selectedEtPosition == 2) {
                     //select next edit text
                     selectedEtPosition = 5;
                     showKeyboard(otp6);
            } else {
                     lanjut.setBackgroundColor(R.drawable.colorlanjut);
                 }
            }

        }
    };
    private void showKeyboard(EditText otp){
        otp.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otp, InputMethodManager.SHOW_IMPLICIT);
    }
    private void starCountDownTimer() {
        resendEnabled = false;
        kirimulang.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000){


            @Override
            public void onTick(long millisUntilFinished) {
                kirimulang.setText("Kirim Ulang ("+(millisUntilFinished / 1000 )+")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                kirimulang.setText("Kirim Ulang");
                kirimulang.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            }
        }.start();
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){

            if(selectedEtPosition ==  5) {

                //select provious edit text
                selectedEtPosition = 4;
                showKeyboard(otp5);

            } else if (selectedEtPosition == 4){
                //select provious edit text
                selectedEtPosition = 3;
                showKeyboard(otp4);
            } else if (selectedEtPosition == 3){
                //select provious edit text
                selectedEtPosition = 2;
                showKeyboard(otp3);
            } else if (selectedEtPosition == 2){
                //select provious edit text
                selectedEtPosition = 1;
                showKeyboard(otp2);
            } else if (selectedEtPosition == 1){
                //select provious edit text
                selectedEtPosition = 0;
                showKeyboard(otp1);
            }
            lanjut.setBackgroundResource(R.drawable.colorlanjut);
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
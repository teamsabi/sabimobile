package com.example.ngikngik;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.api.DbContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class masukkanOTP extends AppCompatActivity {
    private Button btnbatal, lanjut;
    private EditText otp1, otp2, otp3, otp4, otp5;
    private TextView kirimulang;
    private ProgressBar progressBar;
    private int resendTime = 60;
    private boolean resendEnabled = false;
    private int selectedEtPosition = 0;
    private String email;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);  // Menyimpan email yang dimasukkan
        editor.apply();
        // Menghilangkan status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Menetapkan tampilan konten
        setContentView(R.layout.activity_masukkan_otp);

        email = getIntent().getStringExtra("email");
        if (email != null && !email.isEmpty()) {
            editor.putString("email", email);
            editor.apply();
        }


        btnbatal = findViewById(R.id.batalotp);
        lanjut = findViewById(R.id.lanjut);
        progressBar = findViewById(R.id.progressBar2);
        kirimulang = findViewById(R.id.kirimulang);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);

        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);
        otp5.addTextChangedListener(textWatcher);

        btnbatal.setOnClickListener(view -> {
            Intent intent = new Intent(masukkanOTP.this, lupapassword.class);
            startActivity(intent);
        });

        kirimulang.setOnClickListener(view -> {
            if (resendEnabled) {
                String email = preferences.getString("email", "");

                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email tidak valid", Toast.LENGTH_SHORT).show();
                    Log.e("kirimulang", "Email kosong atau tidak valid di SharedPreferences");
                    return;
                }

                startCountDownTimer();
                kirimulang(email);  // Kirim email yang valid
            } else {
                Toast.makeText(getApplicationContext(), "Tunggu beberapa saat untuk mengirim ulang", Toast.LENGTH_SHORT).show();
            }
        });



        lanjut.setOnClickListener(view -> {
            String otp = otp1.getText().toString().trim() +
                    otp2.getText().toString().trim() +
                    otp3.getText().toString().trim() +
                    otp4.getText().toString().trim() +
                    otp5.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(getApplicationContext(), "OTP harus diisi", Toast.LENGTH_SHORT).show();
            } else if (otp.length() != 5) {
                Toast.makeText(getApplicationContext(), "OTP harus terdiri dari 5 digit", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                verifyOTP(otp);
            }
        });

        // Langsung mulai countdown saat halaman dibuka
        startCountDownTimer();
    }

    private void verifyOTP(String otp) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String phpsessid = preferences.getString("PHPSESSID", "");

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_VERIF_OTP_URL,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if ("success".equals(status)) {
                            Intent intent = new Intent(masukkanOTP.this, newpasswordpage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Kesalahan dalam memproses respons server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan jaringan", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("otp", otp);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                if (!phpsessid.isEmpty()) {
                    headers.put("Cookie", "PHPSESSID=" + phpsessid);
                }
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    private void kirimulang(String email) {
        if (email == null || email.isEmpty()) {
            Log.e("kirimulang", "Email tidak valid sebelum mengirim OTP ulang");
            Toast.makeText(getApplicationContext(), "Email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "Otp berhasil dikirim", Toast.LENGTH_SHORT).show();
        Log.d("kirimulang", "Mengirim ulang OTP untuk email: " + email);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_RESEND_OTP_URL,
                response -> {
                    Log.d("kirimulang", "Resend OTP response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if ("success".equals(status)) {
//                            Toast.makeText(getApplicationContext(), "OTP berhasil dikirim ulang", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Kesalahan dalam memproses respons server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("kirimulang", "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Gagal mengirim ulang OTP", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);  // Kirim email yang valid
                return params;
            }
        };

        queue.add(stringRequest);
    }


    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                switch (selectedEtPosition) {
                    case 0:
                        selectedEtPosition = 1;
                        showKeyboard(otp2);
                        break;
                    case 1:
                        selectedEtPosition = 2;
                        showKeyboard(otp3);
                        break;
                    case 2:
                        selectedEtPosition = 3;
                        showKeyboard(otp4);
                        break;
                    case 3:
                        selectedEtPosition = 4;
                        showKeyboard(otp5);
                        break;
                    case 4:
                        lanjut.setEnabled(true);  // Aktifkan tombol "Lanjut" hanya setelah semua digit diisi
                        break;
                }
            }
        }
    };

    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
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
                resendEnabled = true;
                kirimulang.setEnabled(true);
                kirimulang.setText("Kirim Ulang");
                kirimulang.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            }
        }.start();
    }
}

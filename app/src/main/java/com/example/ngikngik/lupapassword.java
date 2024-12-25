package com.example.ngikngik;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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


public class lupapassword extends AppCompatActivity {

    private Dialog dialog;
    private Button btnmengerti;
    private String email;
    private EditText editText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lupapassword);

        dialog = new Dialog(lupapassword.this);
        dialog.setContentView(R.layout.konfirmasiotp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnmengerti = dialog.findViewById(R.id.btn_mengerti);
        progressBar = findViewById(R.id.progress); // ProgressBar

        btnmengerti.setOnClickListener(view -> {
            if (email != null && !email.isEmpty()) {
                Intent intent = new Intent(lupapassword.this, masukkanOTP.class);
                intent.putExtra("email", email);
                startActivity(intent);
                dialog.dismiss();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Email tidak valid atau belum diisi.", Toast.LENGTH_SHORT).show();
            }
        });

        editText = findViewById(R.id.etEmailLupaPw);
        Button buttonlanjut = findViewById(R.id.lanjut);
        Button batal = findViewById(R.id.batalpw);

        batal.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
        });

        buttonlanjut.setOnClickListener(view -> {
            email = editText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Isi kolom email terlebih dahulu", Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(email)) {
                Toast.makeText(getApplicationContext(), "Format email tidak valid", Toast.LENGTH_SHORT).show();
            } else {
                sendEmailRequest(email);
            }
        });
    }

    private boolean isValidEmail(String email) {
        // Regex untuk memvalidasi format email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // x-www-form-urlencoded request using StringRequest
    public void sendEmailRequest(String email) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        progressBar.setVisibility(View.VISIBLE); // Menampilkan ProgressBar

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_LUPA_PASSWORD_URL,
                response -> {
                    // Menangani respons
                    progressBar.setVisibility(View.GONE); // Menyembunyikan ProgressBar
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        if ("success".equals(status)) {
                            dialog.show();
                            // Jika berhasil, tampilkan OTP berhasil dikirim
                        } else {
                            // Jika gagal, tampilkan pesan error
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE); // Menyembunyikan ProgressBar
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan jaringan", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); // Kirimkan email
                return params;
            }
        };

        queue.add(stringRequest);
    }
}

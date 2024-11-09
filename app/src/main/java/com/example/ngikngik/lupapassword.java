package com.example.ngikngik;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class lupapassword extends AppCompatActivity {
    Dialog dialog;
    Button btnmengerti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lupapassword);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dialog = new Dialog(lupapassword.this);
        dialog.setContentView(R.layout.konfirmasiotp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btnmengerti = dialog.findViewById(R.id.btn_mengerti);
        btnmengerti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(lupapassword.this, masukkanOTP.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });



        EditText editText = findViewById(R.id.etEmailLupaPw);
        Button buttonlanjut = findViewById(R.id.lanjut);
        Button batal = findViewById(R.id.batalpw);
        ProgressBar progressBar = findViewById(R.id.progress);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });

        buttonlanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editText.getText().toString().trim();
                if (email.isEmpty()) {
                    // Tampilkan Toast jika kolom kosong
                    Toast.makeText(getApplicationContext(), "Isi kolom email terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else if (!email.endsWith("@gmail.com")) {
                    // Tampilkan Toast jika email tidak berakhiran @gmail.com
                    Toast.makeText(getApplicationContext(), "Format email tidak valid ", Toast.LENGTH_SHORT).show();
                } else {
                    // Lanjutkan permintaan ke server jika kolom tidak kosong
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    progressBar.setVisibility(View.VISIBLE);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_LUPA_PASSWORD_URL,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);
                                    Log.d("Server Response", "Response received: " + response); // Debugging
                                    if (response.contains("Success")) {
                                        Log.d("Dialog", "Displaying dialog"); // Debugging
                                        dialog.show();
                                } else if (response.trim().equals("Email tidak ditemukan")) {
                                        Toast.makeText(getApplicationContext(), "Email belum terdaftar", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d("LupaPassword", "Pesan dari server: " + response);  // Debugging log
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    }


//                                } else
//                                     Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            error.printStackTrace();
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("email", editText.getText().toString());
                            return paramV;
                        }
                    };
                    stringRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 30000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 1;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });
                    queue.add(stringRequest);
                }
            }
        });
    }
}
package com.example.ngikngik;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

        btnmengerti = dialog.findViewById(R.id.btn_mengerti);
        btnmengerti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(lupapassword.this, masukkanOTP.class);
                startActivity(intent);
                dialog.dismiss();
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
                } else {
                    // Lanjutkan permintaan ke server jika kolom tidak kosong
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    progressBar.setVisibility(View.VISIBLE);
                    String url = "http://10.10.181.237/db_sabiproject/resetpassword.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);
                                    if (response.equals("Success")) {
                                        dialog.show();
                                    } else if (response.equals("Email tidak ditemukan")) {
                                        Toast.makeText(getApplicationContext(), "Email tidak ditemukan di database", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Gagal mengirim permintaan. Coba lagi.", Toast.LENGTH_SHORT).show();
                                  }
//                                else
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
package com.example.ngikngik;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.ngikngik.databinding.ActivityDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class lupapassword extends AppCompatActivity {
    Dialog dialog;
    ActivityDashboardBinding binding;
    Button btnmengerti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Menghilangkan status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Menetapkan tampilan konten
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

                                    try {
                                        // Parse JSON response
                                        JSONObject jsonResponse = new JSONObject(response);
                                        String status = jsonResponse.getString("status");
                                        String message = jsonResponse.getString("message");

                                        if (status.equals("success")) {
                                            String phpsessid = jsonResponse.getString("phpsessid");
                                            SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("PHPSESSID", phpsessid);
                                            editor.apply();
                                            // Menyimpan email dan menampilkan dialog
                                            getIntent().putExtra("email", email);
                                            Log.d("Dialog", "Displaying dialog");
                                            dialog.show();
                                        } else {
                                            // Menampilkan pesan error jika status tidak success
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Kesalahan dalam memproses respons server", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Terjadi kesalahan jaringan", Toast.LENGTH_SHORT).show();
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
                            return 20000;
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

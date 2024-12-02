package com.example.ngikngik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.databinding.ActivityDashboardBinding;
import com.example.ngikngik.api.DbContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class newpasswordpage extends AppCompatActivity {
    private EditText newPassword, confirmPassword;
    private Button savePasswordButton;
    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Menghilangkan status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Menetapkan tampilan konten
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_newpasswordpage);

        newPassword = findViewById(R.id.sandi_baru);
        confirmPassword = findViewById(R.id.konfirmasi_sandi_baru);
        savePasswordButton = findViewById(R.id.btnsavepassword);

        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = newPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                // Regex untuk validasi password
                String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";

                if (newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap isi kolom", Toast.LENGTH_SHORT).show();
                } else if (!newPass.matches(passwordPattern)) {
                    Toast.makeText(getApplicationContext(), "Kata sandi harus berisi minimal 6 karakter yang terdiri dari huruf dan angka!", Toast.LENGTH_SHORT).show();
                } else if (!newPass.equals(confirmPass)) {
                    Toast.makeText(getApplicationContext(), "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                } else {
                    // Ambil PHPSESSID dari SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String phpsessid = preferences.getString("PHPSESSID", "");

                    // Melakukan request untuk mengganti password
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_NEWPASSWORD_URL,
                            response -> {
                                Log.d("Server Response", response);
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String status = jsonResponse.getString("status");
                                    String message = jsonResponse.getString("message");

                                    if ("success".equals(status)) {
                                        Intent intent = new Intent(newpasswordpage.this, login.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Password berhasil diganti", Toast.LENGTH_SHORT).show();
                                        finish();  // Tutup halaman setelah berhasil
                                    } else {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Kesalahan dalam memproses respons server", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> {
                                Log.e("Network Error", error.toString());
                                Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("password", newPass); // Kirimkan password baru
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<>();
                            if (!phpsessid.isEmpty()) {
                                headers.put("Cookie", "PHPSESSID=" + phpsessid); // Sertakan PHPSESSID
                            }
                            return headers;
                        }
                    };

                    queue.add(stringRequest);
                }
            }
        });
    }
}


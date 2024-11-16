package com.example.ngikngik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class newpasswordpage extends AppCompatActivity {
    private EditText newPassword, confirmPassword;
    private Button savePasswordButton;
    private String email, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpasswordpage);

        newPassword = findViewById(R.id.sandi_baru);
        confirmPassword = findViewById(R.id.konfirmasi_sandi_baru);
        savePasswordButton = findViewById(R.id.btnsavepassword);

        // Ambil email dan OTP dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("email", null);


        // Cek jika otp atau email kosong
//        if (email == null || otp == null) {
//            Toast.makeText(this, "Error: Email atau OTP tidak ditemukan", Toast.LENGTH_SHORT).show();
//            finish();  // Keluar dari halaman jika email atau OTP tidak valid
//        }

        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = newPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap isi kolom", Toast.LENGTH_SHORT).show();
                } else if (!newPass.equals(confirmPass)) {
                    Toast.makeText(getApplicationContext(), "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                }  else {
                    // Cek apakah email valid sebelum mencoba update password
                    if (email != null && !email.isEmpty()) {
                        // Call updatePassword jika form valid
                        updatePassword(newPass);
                    } else {
                        Toast.makeText(getApplicationContext(), "Email tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updatePassword(String newPass) {
        RequestQueue queue = Volley.newRequestQueue(this);

        // URL API PHP untuk mengganti password
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_NEWPASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Server Response", response);  // Cek respons server yang diterima
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if ("success".equals(status)) {
                                Toast.makeText(getApplicationContext(), "Password telah diganti", Toast.LENGTH_SHORT).show();
                                finish(); // Tutup halaman setelah berhasil
                            } else {
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
                Log.e("Network Error", error.toString());
                Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); // Kirimkan email pengguna untuk referensi di server
                params.put("password", newPass); // Kirimkan password baru
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
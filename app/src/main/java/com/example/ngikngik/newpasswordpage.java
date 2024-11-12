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

import java.util.HashMap;
import java.util.Map;

public class newpasswordpage extends AppCompatActivity {
    private EditText newPassword, confirmPassword;
    private Button savePasswordButton;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpasswordpage);

        newPassword = findViewById(R.id.sandi_baru);
        confirmPassword = findViewById(R.id.konfirmasi_sandi_baru);
        savePasswordButton = findViewById(R.id.btnsavepassword);

        // Ambil email dari intent jika dikirim dari halaman sebelumnya
//        email = getIntent().getStringExtra("email");

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        email = sharedPreferences.getString("email", null);

        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = newPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap isi kolom", Toast.LENGTH_SHORT).show();
                } else if (!newPass.equals(confirmPass)) {
                    Toast.makeText(getApplicationContext(), "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                } else {
                    updatePassword(newPass);
//                    Intent intent = new Intent(newpasswordpage.this, login.class );
//                    startActivity(intent);
                }
            }

        });
//        Intent intent = new Intent(newpasswordpage.this, login.class);
//        intent.putExtra("email", email);  // Pastikan 'email' memiliki nilai yang valid sebelum mengirim
//        startActivity(intent);
//
//        email = getIntent().getStringExtra("email");
//        if (email == null || email.isEmpty()) {
//            Toast.makeText(this, "Error: Email tidak ditemukan", Toast.LENGTH_SHORT).show();
//            finish();  // Keluar dari halaman jika email tidak valid
//            return;
//        }
    }


    private void updatePassword(String newPassword) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_NEWPASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Log.d("Server Response", response); // Tambahkan log ini untuk debugging
                        if (response.contains("Password Berhasil diubah")) {
                            Toast.makeText(getApplicationContext(), "Password telah diganti", Toast.LENGTH_SHORT).show();
                            finish();  // Tutup halaman setelah berhasil
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal memperbarui kata sandi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); // Kirim email pengguna
                params.put("password", newPassword);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
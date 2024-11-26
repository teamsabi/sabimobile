package com.example.ngikngik;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private EditText etEmail, etPassRegister, etVerificationPassword;
    private ProgressDialog progressDialog;
    private CheckBox checkBoxJenjang, checkBoxKelas11, checkBoxKelas12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Menghilangkan status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        // Inisialisasi view
        etEmail = findViewById(R.id.etEmailRegister);
        etPassRegister = findViewById(R.id.etPasswordRegister);
        etVerificationPassword = findViewById(R.id.etVerificationPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView txtMasuk = findViewById(R.id.txt_masuk);
        progressDialog = new ProgressDialog(this);

        checkBoxJenjang = findViewById(R.id.jenjangcheck2); // SMA
        checkBoxKelas11 = findViewById(R.id.kelas); // Kelas 11
        checkBoxKelas12 = findViewById(R.id.kelas2); // Kelas 12

        progressDialog.setMessage("Memproses...");
        progressDialog.setCancelable(false);

        // Aksi tombol masuk
        txtMasuk.setOnClickListener(view -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
        });

        // Aksi tombol registrasi
        btnRegister.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassRegister.getText().toString().trim();
            String verifyPassword = etVerificationPassword.getText().toString().trim();

            // Validasi data
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyPassword)) {
                Toast.makeText(register.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(register.this, "Email tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(register.this, "Password harus minimal 6 karakter", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(verifyPassword)) {
                Toast.makeText(register.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ambil data dari checkbox
            String jenjang = checkBoxJenjang.isChecked() ? "SMA" : "";
            String kelas = checkBoxKelas11.isChecked() ? "11" : checkBoxKelas12.isChecked() ? "12" : "";

            if (TextUtils.isEmpty(jenjang) || TextUtils.isEmpty(kelas)) {
                Toast.makeText(register.this, "Harap pilih jenjang dan kelas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim data ke server
            CreateDataToServer(email, password, jenjang, kelas);
        });
    }

    private void CreateDataToServer(final String email, final String password, final String jenjang, final String kelas) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String serverResponse = jsonObject.getString("server_response");
                            if (serverResponse.contains("\"status\":\"OK\"")) {
                                Toast.makeText(register.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(register.this, "Registrasi gagal: " + serverResponse, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(register.this, "Kesalahan JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } finally {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    },
                    error -> {
                        Toast.makeText(register.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("jenjang", jenjang);
                    params.put("kelas", kelas);
                    return params;
                }
            };

            VolleyConnection.getInstance(this).addToRequestQue(stringRequest);
        } else {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

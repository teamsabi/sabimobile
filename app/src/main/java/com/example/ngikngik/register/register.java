package com.example.ngikngik.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ngikngik.R;
import com.example.ngikngik.VolleyConnection;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class register extends AppCompatActivity {

    private EditText etEmail, etPassRegister, etVerificationPassword;
    private Spinner spinnerKelas;
    private ProgressDialog progressDialog;
    private Map<String, String> kelasMap; // Untuk menyimpan nama_kelas -> kode_kelas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        // Initialize Views
        etEmail = findViewById(R.id.etEmailRegister);
        etPassRegister = findViewById(R.id.etPasswordRegister);
        etVerificationPassword = findViewById(R.id.etVerificationPassword);
        spinnerKelas = findViewById(R.id.spinnerKelas);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView txtMasuk = findViewById(R.id.txt_masuk);

        // Move to Login
        txtMasuk.setOnClickListener(view -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
        });

        // Fetch class data
        fetchKelasData();

        // Register Button Click
        btnRegister.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassRegister.getText().toString().trim();
            String verifyPassword = etVerificationPassword.getText().toString().trim();
            String selectedKelas = spinnerKelas.getSelectedItem() != null ? spinnerKelas.getSelectedItem().toString() : "";

            if (validateInput(email, password, verifyPassword, selectedKelas)) {
                // Dapatkan kode kelas berdasarkan nama kelas
                String kodeKelas = kelasMap.get(selectedKelas);
                createDataToServer(email, password, kodeKelas);
            }
        });
    }

    private boolean validateInput(String email, String password, String verifyPassword, String selectedKelas) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyPassword)) {
            Toast.makeText(this, "Silahkan Isi semua kolom", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Kata sandi minimal harus 6 karakter", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(verifyPassword)) {
            Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(selectedKelas)) {
            Toast.makeText(this, "Harap pilih kelas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void fetchKelasData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_GET_KELAS,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.optString("status", "error");

                        if ("success".equals(status)) {
                            JSONArray kelasArray = jsonObject.optJSONArray("kelas");
                            if (kelasArray != null) {
                                List<String> kelasList = new ArrayList<>();
                                kelasMap = new HashMap<>();
                                for (int i = 0; i < kelasArray.length(); i++) {
                                    JSONObject kelas = kelasArray.optJSONObject(i);
                                    String kodeKelas = kelas.optString("kode_kelas", "Unknown");
                                    String namaKelas = kelas.optString("nama_kelas", "Unknown");
                                    kelasMap.put(namaKelas, kodeKelas);
                                    kelasList.add(namaKelas);
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                        android.R.layout.simple_spinner_item, kelasList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerKelas.setAdapter(adapter);
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Gagal parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Gagal memuat data kelas", Toast.LENGTH_SHORT).show();
                });

        VolleyConnection.getInstance(this).addToRequestQue(stringRequest);
    }

    private void createDataToServer(String email, String password, String kodeKelas) {
        progressDialog.setMessage("Mendaftarkan...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if ("success".equals(status)) {
                            startActivity(new Intent(register.this, login.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Gagal parsing respons: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("kode_kelas", kodeKelas);
                return params;
            }
        };

        VolleyConnection.getInstance(this).addToRequestQue(stringRequest);
    }
}

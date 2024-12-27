package com.example.ngikngik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ngikngik.Dashboard.dashboard;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.register.register;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class login extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView lupapw, textViewLogin;
    private ProgressDialog progressDialog;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "IsLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek apakah pengguna sudah login
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            navigateToDashboard();
            return;
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btn_login2);
        progressDialog = new ProgressDialog(login.this);
        lupapw = findViewById(R.id.lupapw);
        textViewLogin = findViewById(R.id.txt_donthaveaccount);

        ImageView imageViewShowHidePw = findViewById(R.id.imageView_show_hide_pw);
        imageViewShowHidePw.setImageResource(R.drawable.tutupmatapw);
        imageViewShowHidePw.setOnClickListener(view -> {
            int cursorPosition = etPassword.getSelectionStart();
            if (etPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewShowHidePw.setImageResource(R.drawable.tutupmatapw);
            } else {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidePw.setImageResource(R.drawable.tampilmatapw);
            }
            etPassword.setSelection(cursorPosition);
        });

        textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), register.class);
            startActivity(intent);
        });

        lupapw.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), lupapassword.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email atau password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            CheckLogin(email, password);
        });
    }

    public void CheckLogin(final String email, final String password) {
        if (checkNetworkConnection()) {
            progressDialog.setMessage("Sedang login...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_LOGIN_URL,
                    response -> {
                        Log.d("LoginResponse", "Server Response: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                String serverResponse = jsonObject.getString("server_response");
                                if (serverResponse.equals("login berhasil")) {
                                    String userId = jsonObject.getString("user_id");

                                    // Ambil daftar kelas dari respons server
                                    JSONArray classesArray = jsonObject.getJSONArray("classes");
                                    List<String> kelasList = new ArrayList<>();
                                    for (int i = 0; i < classesArray.length(); i++) {
                                        JSONObject classObject = classesArray.getJSONObject(i);
                                        String className = classObject.getString("nama_kelas");
                                        kelasList.add(className);
                                    }

                                    // Simpan email, kelas, dan user_id ke SharedPreferences
                                    // In CheckLogin method of login activity
                                    SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                    editor.putString("email", email);  // Store email
                                    editor.putString("id_user", userId); // Store user_id
                                    editor.putString("kelas", new Gson().toJson(kelasList)); // Store class list
                                    editor.apply();


                                    Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_SHORT).show();
                                    navigateToDashboard();
                                }
                            } else {
                                String errorMessage = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Terjadi kesalahan dalam parsing respon server", Toast.LENGTH_SHORT).show();
                        } finally {
                            progressDialog.dismiss();
                        }
                    }, error -> {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan, coba lagi", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            VolleyConnection.getInstance(login.this).addToRequestQue(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void navigateToDashboard() {
        Intent dashboardIntent = new Intent(login.this, dashboard.class);
        startActivity(dashboardIntent);
        finish();
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

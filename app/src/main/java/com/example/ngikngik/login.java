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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ngikngik.Dashboard.dashboard;
import com.example.ngikngik.api.DbContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
        imageViewShowHidePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPosition = etPassword.getSelectionStart();
                if (etPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePw.setImageResource(R.drawable.tutupmatapw);
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePw.setImageResource(R.drawable.tampilmatapw);
                }
                etPassword.setSelection(cursorPosition);
            }
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
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            CheckLogin(email, password);
        });
    }

    public void CheckLogin(final String email, final String password) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_LOGIN_URL,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String serverResponse = jsonObject.getString("server_response");

                            if (serverResponse.equals("login berhasil")) {
                                String kelas = jsonObject.getString("kelas");
                                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                editor.putString("kelas", kelas);
                                editor.putString("email", email);
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_SHORT).show();
                                navigateToDashboard();
                            } else {
                                Toast.makeText(getApplicationContext(), "Email dan password salah", Toast.LENGTH_SHORT).show();
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

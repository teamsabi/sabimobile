package com.example.ngikngik.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
    private ProgressDialog progressDialog;
    private Spinner spinnerKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        // Initialize views
        etEmail = findViewById(R.id.etEmailRegister);
        etPassRegister = findViewById(R.id.etPasswordRegister);
        etVerificationPassword = findViewById(R.id.etVerificationPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView txtMasuk = findViewById(R.id.txt_masuk);
        spinnerKelas = findViewById(R.id.spinnerKelas);


        // Action for login button
        txtMasuk.setOnClickListener(view -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
        });

        // Fetch class data from the server when the page loads
        fetchKelasData();

        // Action for register button
        btnRegister.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassRegister.getText().toString().trim();
            String verifyPassword = etVerificationPassword.getText().toString().trim();

            // Validate input data
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verifyPassword)) {
                Toast.makeText(register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(register.this, "Invalid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(register.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(verifyPassword)) {
                Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected class
            String selectedKelas = spinnerKelas.getSelectedItem() != null ? spinnerKelas.getSelectedItem().toString() : "";
            if (TextUtils.isEmpty(selectedKelas)) {
                Toast.makeText(register.this, "Harap pilih kelas", Toast.LENGTH_SHORT).show();
                return;
            }


            // Send data to the server
            CreateDataToServer(email, password);
        });
    }

    private void fetchKelasData() {
        if (checkNetworkConnection()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_GET_KELAS,
                    response -> {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if ("success".equals(status)) {
                                JSONArray kelasArray = jsonObject.getJSONArray("kelas");
                                List<String> kelasList = new ArrayList<>();

                                for (int i = 0; i < kelasArray.length(); i++) {
                                    kelasList.add(kelasArray.getString(i));
                                }

                                // Update Spinner with data
                                ArrayAdapter<String> kelasAdapter = new ArrayAdapter<>(this,
                                        android.R.layout.simple_spinner_item, kelasList);
                                kelasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerKelas.setAdapter(kelasAdapter);
                            } else {
                                Toast.makeText(this, "Gagal mendapatkan data kelas", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            VolleyConnection.getInstance(this).addToRequestQue(stringRequest);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void CreateDataToServer(final String email, final String password) {
                if (checkNetworkConnection()) {
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                            response -> {
                                // Handle successful response
                            },
                            error -> {
                                // Handle error response
                                Toast.makeText(register.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("password", password);
                            return params;
                        }
                    };

// Set timeout for the request
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            5000, // Timeout in milliseconds
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Number of retries
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Multiplier for backoff
                    ));

                    VolleyConnection.getInstance(this).addToRequestQue(stringRequest);

                } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

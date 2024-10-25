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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class register extends AppCompatActivity {

    Button buttonSignup;
    TextView textViewLogin;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    private EditText etBirthdate;
    private EditText etFullname, etUsername, etEmail, etPassword, etVerificationPassword, etAddress, etPhone;
    private Spinner spinnerGender;
    private Button btnRegister;

    public void CreateDataToServer(final String username, final String email, final String password, final String gender, final String tanggal_lahir, final String alamat, final String nomertelepon) {
        if (checkNetworkConnection()) {
            progressDialog.show();  // Tampilkan dialog sebelum memulai permintaan
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("response", response.toString());
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"OK\"}]")) {
                                    Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Log.e("JSON Error", e.toString());
                            } finally {
                                // Pastikan untuk menutup dialog di sini
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("gender", gender);
                    params.put("tanggal_lahir", tanggal_lahir);
                    params.put("alamat", alamat);
                    params.put("nomertelepon", nomertelepon);
                    return params;
                }
            };

            VolleyConnection.getInstance(register.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        }else {
            Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }

    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            progressDialog.setMessage("Memproses..."); // Menambahkan pesan untuk dialog
            progressDialog.setCancelable(false); // Menonaktifkan kemampuan untuk membatalkan dialog

            return insets;
        });

//        db=new SQLiteHandler(getApplicationContext()) {
//        }


//        etFullname = (EditText) findViewById(R.id.etFullname);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmailRegister);
        etPassword = (EditText) findViewById(R.id.etPasswordRegister);
        etVerificationPassword = (EditText)  findViewById(R.id.etVerificationPassword);
        etBirthdate = (EditText) findViewById(R.id.etBirthdate);
        etAddress = (EditText) findViewById(R.id.etAddress);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etPhone = (EditText) findViewById(R.id.etPhone);
        progressDialog = new ProgressDialog(register.this);

        etBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


//                        }
//                        //End Write and Read data with URL
//                    }
//                });    //End Write and Read data with URL
//             }
        btnRegister.setOnClickListener(new View.OnClickListener() {
            private Object username;

            @Override
            public void onClick(View v) {
//                String fullname = etFullname.getText().toString();
                String Susername = etUsername.getText().toString();
                String Semail = etEmail.getText().toString();
                String Spassword = etPassword.getText().toString();
                String SverifyPassword = etVerificationPassword.getText().toString();
                String Sbirthdate = etBirthdate.getText().toString();
                String Saddress = etAddress.getText().toString();
                String Sgender = spinnerGender.getSelectedItem().toString();
                String Sphone = etPhone.getText().toString();
                if (Susername.isEmpty() || Spassword.isEmpty() || Sbirthdate.isEmpty() || Saddress.isEmpty()) {
                    Toast.makeText(register.this, "Isi Semua Kolom di atas", Toast.LENGTH_SHORT).show();
                } else if (Sphone.isEmpty()) {
                    Toast.makeText(register.this, "Masukkan nomor telepon", Toast.LENGTH_SHORT).show();
                } else if (!Spassword.equals(SverifyPassword)) {
                    Toast.makeText(register.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                } else if (!android.text.TextUtils.isDigitsOnly(Sphone)) {
                    Toast.makeText(register.this, "Nomor telepon hanya boleh berisi angka", Toast.LENGTH_SHORT).show();
                } else {
                    CreateDataToServer(Susername, Semail, Spassword, Sgender, Sbirthdate, Saddress, Sphone);
                    Toast.makeText(register.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


    public boolean checkNetworkConnection() {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etBirthdate.setText(year + "-" + (month + 1) + "-" +dayOfMonth );
                    }
                },
                year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
}

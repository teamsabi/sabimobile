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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.ngikngik.Dashboard.beranda;
import com.example.ngikngik.Dashboard.fragment_daftar;
import com.example.ngikngik.databinding.ActivityDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class register extends AppCompatActivity {

    Button buttonSignup;
    ActivityDashboardBinding binding;
    TextView textViewLogin, txtmasuk;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    private EditText etBirthdate;
    private EditText  etEmail, etPassRegister, etVerificationPassword;
    private Button btnRegister;

    public void CreateDataToServer(final String email, final String password) {
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
                    params.put("email", email);
                    params.put("password", password);
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

        // Menghilangkan status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Menetapkan tampilan konten
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            progressDialog.setMessage("Memproses..."); // Menambahkan pesan untuk dialog
            progressDialog.setCancelable(false); // Menonaktifkan kemampuan untuk membatalkan dialog

            return insets;
        });
//        //verifmata
        ImageView showhide = findViewById(R.id.showhideverif);
        showhide.setImageResource(R.drawable.tutupmatapw);
        showhide.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etVerificationPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    etVerificationPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showhide.setImageResource(R.drawable.tutupmatapw);
                }else {
                    etVerificationPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showhide.setImageResource(R.drawable.tampilmatapw);
                }
            }
        });
        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Simpan posisi kursor saat ini
                int cursorPosition = etVerificationPassword.getSelectionStart();

                if (etVerificationPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    // Ganti ke mode sembunyi password
                    etVerificationPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showhide.setImageResource(R.drawable.tutupmatapw);
                } else {
                    // Ganti ke mode tampil password
                    etVerificationPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showhide.setImageResource(R.drawable.tampilmatapw);
                }

                // Kembalikan posisi kursor ke tempat sebelumnya
                etVerificationPassword.setSelection(cursorPosition);
            }
        });
        //katasandi
        ImageView imageViewShowHidePw = findViewById(R.id.imageView_show_hide_pw);
        imageViewShowHidePw.setImageResource(R.drawable.tutupmatapw);
        imageViewShowHidePw.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageViewShowHidePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassRegister.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    etPassRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePw.setImageResource(R.drawable.tutupmatapw);
                }else {
                    etPassRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePw.setImageResource(R.drawable.tampilmatapw);
                }
            }
        });
        imageViewShowHidePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Simpan posisi kursor saat ini
                int cursorPosition = etPassRegister.getSelectionStart();

                if (etPassRegister.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    // Ganti ke mode sembunyi password
                    etPassRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePw.setImageResource(R.drawable.tutupmatapw);
                } else {
                    // Ganti ke mode tampil password
                    etPassRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePw.setImageResource(R.drawable.tampilmatapw);
                }

                // Kembalikan posisi kursor ke tempat sebelumnya
                etPassRegister.setSelection(cursorPosition);
            }
        });

//        db=new SQLiteHandler(getApplicationContext()) {
//        }

        txtmasuk = findViewById(R.id.txt_masuk);
        etEmail = (EditText) findViewById(R.id.etEmailRegister);
        etPassRegister = (EditText) findViewById(R.id.etPasswordRegister);
        etVerificationPassword = (EditText)  findViewById(R.id.etVerificationPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(register.this);

//        etBirthdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePickerDialog();
//            }
//        });


//                        }
//                        //End Write and Read data with URL
//                    }
//                });    //End Write and Read data with URL
//             }
        txtmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            private Object username;

            @Override
            public void onClick(View v) {

                String Semail = etEmail.getText().toString();
                String Spassword = etPassRegister.getText().toString();
                String SverifyPassword = etVerificationPassword.getText().toString();

                if (Spassword.isEmpty()) {
                    Toast.makeText(register.this, "Isi Semua Kolom di atas", Toast.LENGTH_SHORT).show();
                } else if (!Spassword.equals(SverifyPassword)) {
                    Toast.makeText(register.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                } else {
                    CreateDataToServer(Semail, Spassword);
                    Toast.makeText(register.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(register.this, fragment_daftar.class);
                    startActivity(intent);
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

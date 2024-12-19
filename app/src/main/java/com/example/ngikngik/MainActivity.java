package com.example.ngikngik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ngikngik.Dashboard.dashboard;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "IsLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengecek apakah pengguna sudah login
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            // Jika sudah login, langsung menuju ke Dashboard
            navigateToDashboard();
        } else {
            // Jika belum login, arahkan ke layar Login
            navigateToLogin();
        }
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(MainActivity.this, dashboard.class);
        startActivity(intent);
        finish(); // Menutup MainActivity agar pengguna tidak bisa kembali
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, login.class);
        startActivity(intent);
        finish(); // Menutup MainActivity agar pengguna tidak bisa kembali
    }
}

package com.example.ngikngik.Dashboard;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ngikngik.R;
import com.example.ngikngik.berandaygy.beranda;
import com.example.ngikngik.Raport.raport;
import com.example.ngikngik.databinding.ActivityDashboardBinding;
import com.example.ngikngik.profil.profile_akun;

public class dashboard extends AppCompatActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Langsung tampilkan fragment beranda saat pertama kali memasuki dashboard
        if (savedInstanceState == null) {
            replaceFragment(new beranda()); // Menampilkan beranda pertama kali
        }

        // Pilih item 'beranda' secara otomatis saat pertama kali aplikasi dijalankan
        binding.bottomNavigation.setSelectedItemId(R.id.beranda);

        // Set background to null and handle navigation item clicks
        binding.bottomNavigation.setBackground(null);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.beranda) {
                replaceFragment(new beranda());
            } else if (item.getItemId() == R.id.profil) {
                replaceFragment(new profile_akun());
            } else if (item.getItemId() == R.id.raport) {
                replaceFragment(new raport());
            }
            return true;
        });
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
}
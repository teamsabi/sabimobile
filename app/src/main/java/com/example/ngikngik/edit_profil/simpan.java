package com.example.ngikngik.edit_profil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ngikngik.R;
import com.example.ngikngik.databinding.ActivitySimpanBinding;

public class simpan extends AppCompatActivity {

    ActivitySimpanBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimpanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        // Langsung tampilkan fragment beranda saat pertama kali memasuki dashboard
        if (savedInstanceState == null) {
            replaceFragment(new profil_edit()); // Menampilkan beranda pertama kali
        }
        binding.bottomNavigationSimpan.setBackground(null);
        binding.bottomNavigationSimpan.setOnItemSelectedListener(item -> {
            Log.d("DEBUG", "Item ID: " + item.getItemId());
            if (item.getItemId() == R.id.simpann) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framelayoutsimpan);
                Log.d("DEBUG", "Current Fragment: " + currentFragment);
                if (currentFragment instanceof profil_edit) {
                    profil_edit profilEditFragment = (profil_edit) currentFragment;
                    if (profilEditFragment.validateFields()) {
                        Log.d("DEBUG", "Validation successful!");
                        saveData();
                        return true;
                    } else {
                        Log.d("DEBUG", "Validation failed.");
                        return false;
                    }
                }
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutsimpan, fragment);
        fragmentTransaction.commit();
    }

    private void saveData() {
        // Logika untuk menyimpan data ke database atau backend
        Log.d("DEBUG", "Data berhasil disimpan!");
    }
}
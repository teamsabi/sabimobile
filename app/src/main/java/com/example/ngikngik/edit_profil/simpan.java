package com.example.ngikngik.edit_profil;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ngikngik.R;

public class simpan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan);

        // Set default fragment
        loadFragment(new profil_edit());

        // Tombol Simpan klik listener
        findViewById(R.id.save_button).setOnClickListener(v -> {
            // Tambahkan logika tombol simpan
            // Contoh: Menyimpan data atau validasi input
            System.out.println("Tombol Simpan diklik!");
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}

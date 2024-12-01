package com.example.ngikngik.edit_profil;

import android.os.Bundle;
import android.widget.Toast;
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
            // Cari fragment yang aktif saat ini
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (currentFragment instanceof profil_edit) {
                profil_edit fragment = (profil_edit) currentFragment;

                // Validasi input di fragment profil_edit
                if (fragment.validateFields()) {
                    // Jika validasi berhasil, tampilkan Toast dan lakukan logika simpan
                    Toast.makeText(simpan.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();

                    // Logika untuk menyimpan data, bisa berupa simpan ke database atau server
                    // Misalnya, sharedPreferences untuk menyimpan data sementara
                    // sharedPreferences.edit().putString("nama", fragment.getNama()).apply();

                    System.out.println("Tombol Simpan diklik!");
                } else {
                    // Jika validasi gagal, tampilkan pesan kesalahan
                    Toast.makeText(simpan.this, "silahkan isi kolom terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}

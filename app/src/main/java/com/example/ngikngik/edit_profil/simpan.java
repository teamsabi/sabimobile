package com.example.ngikngik.edit_profil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ngikngik.R;
import com.example.ngikngik.Dashboard.dashboard; // Pastikan ini sesuai dengan nama Activity Dashboard Anda

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

            // Periksa apakah fragment yang aktif adalah profil_edit
            if (currentFragment instanceof profil_edit) {
                profil_edit fragment = (profil_edit) currentFragment;

                // Validasi input di fragment profil_edit
                if (fragment.validateFields()) {
                    // Jika validasi berhasil, tampilkan Toast dan lakukan logika simpan
                    Toast.makeText(simpan.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();

                    // Simpan data ke SharedPreferences atau database
                    fragment.saveProfileData(); // Panggil metode untuk menyimpan data yang ada di profil_edit

                    // Logika untuk melanjutkan setelah data berhasil disimpan
                    goToDashboard(); // Navigasi ke halaman Dashboard atau halaman lain yang diinginkan
                } else {
                    // Jika validasi gagal, tampilkan pesan kesalahan
                    Toast.makeText(simpan.this, "Silahkan isi kolom yang belum terisi", Toast.LENGTH_SHORT).show();
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

    // Fungsi untuk kembali ke halaman Dashboard atau halaman lainnya
    private void goToDashboard() {
        // Menavigasi ke Activity Dashboard setelah data disimpan
        Intent intent = new Intent(simpan.this, dashboard.class);
        startActivity(intent);

        // Jika Anda ingin menutup activity saat ini dan kembali ke activity sebelumnya
        finish();
    }
}

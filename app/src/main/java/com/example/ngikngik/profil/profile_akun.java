package com.example.ngikngik.profil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ngikngik.Adapter.NameAdapter;
import com.example.ngikngik.Adapter.ClassAdapter;
import com.example.ngikngik.edit_profil.item_class;
import com.example.ngikngik.R;
import com.example.ngikngik.edit_profil.profil_edit; // Pastikan import ini benar
import com.example.ngikngik.login;

import java.util.ArrayList;
import java.util.List;

public class profile_akun extends Fragment {
    private RecyclerView rvNamaAkun, rvKelasAkun;
    private NameAdapter nameAdapter;
    private ClassAdapter classAdapter;
    private ImageView imageView;
    private TextView txtlogout;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_akun, container, false);

        // Menyembunyikan sistem UI untuk pengalaman fullscreen
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Inisialisasi SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        // Inisialisasi RecyclerView untuk nama dan kelas
        rvNamaAkun = view.findViewById(R.id.rvNamaAkun);
        rvKelasAkun = view.findViewById(R.id.rvKelasAkun);

        // Membuat dan mengatur adapter untuk nama dan kelas
        List<item_name> nameList = new ArrayList<>();
        nameList.add(new item_name(nama));
        nameAdapter = new NameAdapter(nameList);
        rvNamaAkun.setAdapter(nameAdapter);
        rvNamaAkun.setLayoutManager(new LinearLayoutManager(getContext()));


        List<item_class> classList = new ArrayList<>();
        classList.add(new item_class(kelas));
        classAdapter = new ClassAdapter(classList, classItem -> {
            Log.d("ClassAdapter", "Class clicked: " + classItem.getClassName());
        });
        rvKelasAkun.setAdapter(classAdapter);
        rvKelasAkun.setLayoutManager(new LinearLayoutManager(getContext()));

        // Menangani klik tombol edit profil
        imageView = view.findViewById(R.id.btnEdit);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), profil_edit.class);
            startActivity(intent);
        });

        // Menangani klik tombol logout
        txtlogout = view.findViewById(R.id.txtLogout);
        txtlogout.setOnClickListener(v -> {
            showLogoutDialog();
        });

        return view;
    }

    private void showLogoutDialog() {
        // Membuat dan menampilkan dialog konfirmasi logout
        new AlertDialog.Builder(getContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performLogout(); // Menjalankan logout setelah konfirmasi
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void performLogout() {
        // Mengambil SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Menghapus semua data yang tersimpan (seperti nama, kelas, dll)
        editor.clear();  // Menghapus semua data
        editor.apply();   // Menerapkan perubahan

        // Redirect ke halaman login
        Intent intent = new Intent(getActivity(), login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Menghapus semua aktivitas sebelumnya
        startActivity(intent);

        // Menyelesaikan aktivitas ini (agar tidak kembali ke halaman sebelumnya)
        requireActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Memperbarui data saat fragment resume
        String nama = sharedPreferences.getString("nama", "Nama Default");
        String kelas = sharedPreferences.getString("kelas", "Kelas Default");

        // Memperbarui data RecyclerView dengan nilai terbaru
        if (nameAdapter != null) {
            List<item_name> nameList = new ArrayList<>();
            nameList.add(new item_name(nama));
            nameAdapter.updateData(nameList);
        }

        if (classAdapter != null) {
            List<item_class> classList = new ArrayList<>();
            classList.add(new item_class(kelas));
            classAdapter.updateData(classList);
        }
    }
}

package com.example.ngikngik.Dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ngikngik.R;
import com.example.ngikngik.edit_profil.simpan;

public class profile_akun extends Fragment {
    private ImageView imageView;
    private TextView rvNamaAkun, rvKelasAkun;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil_akun, container, false);

        // Menghilangkan status bar
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        // Inisialisasi View
        imageView = view.findViewById(R.id.btnEdit);
        rvNamaAkun = view.findViewById(R.id.rvNamaAkun);
        rvKelasAkun = view.findViewById(R.id.rvKelasAkun);

        // Mengambil data nama dan kelas dari SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        // Menampilkan data di TextView
        rvNamaAkun.setText(nama);
        rvKelasAkun.setText(kelas);

        // Navigasi ke halaman edit profil
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), simpan.class);
            startActivity(intent);
        });

        return view;
    }
}

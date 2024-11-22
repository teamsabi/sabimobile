package com.example.ngikngik.Dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.ngikngik.R;

public class fragment_daftar extends Fragment {

    public fragment_daftar() {
        super(R.layout.fragment_daftar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Mengakses elemen UI
        CheckBox kelas11 = view.findViewById(R.id.kelas11);
        CheckBox kelas12 = view.findViewById(R.id.kelas12);

        // Listener untuk memastikan hanya satu checkbox dapat dipilih
        kelas11.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                kelas12.setChecked(false);
            }
        });

        kelas12.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                kelas11.setChecked(false);
            }
        });
    }
}

package com.example.ngikngik.Dashboard;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ngikngik.R;


public class DaftarDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Buat dialog dan tampilkan tampilan dari layout daftar_berhasil
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.daftarberhasil);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button lanjut = dialog.findViewById(R.id.btnlanjutdaftar);
        lanjut.setOnClickListener(v -> {
            // Pindah ke activity beranda setelah klik tombol
            Intent intent = new Intent(getActivity(), beranda.class);
            startActivity(intent);
            dialog.dismiss();
            getActivity().finish(); // Pastikan menutup activity saat pindah
        });

        return dialog;
    }
}

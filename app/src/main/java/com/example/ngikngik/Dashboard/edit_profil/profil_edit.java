package com.example.ngikngik.Dashboard.edit_profil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.Dashboard.dashboard;
import com.example.ngikngik.R;

import java.util.ArrayList;
import java.util.Calendar;

public class profil_edit extends Fragment {
    private ImageView imageView;
    private EditText etBirhdate;
    private RecyclerView recyclerView;
    private ClassAdapter kelasAdapter;
    private ArrayList<item_class> kelasList;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_edit, container, false);
        Log.d("DEBUG", "profil_edit fragment loaded.");

        // Inisialisasi View
        imageView = view.findViewById(R.id.imgBack);
        etBirhdate = view.findViewById(R.id.etBirthDate);
        recyclerView = view.findViewById(R.id.rvKelas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        // Inisialisasi kelasList
        kelasList = new ArrayList<>();
        kelasList.add(new item_class(kelas)); // Tambahkan data kelas ke dalam kelasList

        // Inisialisasi adapter dengan listener klik item
        kelasAdapter = new ClassAdapter(kelasList, classItem -> {
            // Aksi ketika item kelas diklik
            Log.d("DEBUG", "Kelas yang dipilih: " + classItem.getClassName());
        });

        recyclerView.setAdapter(kelasAdapter);

        // Event listener untuk tombol kembali
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), dashboard.class);
            startActivity(intent);
        });

        // Event listener untuk memilih tanggal
        etBirhdate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (datePicker, year1, month1, day1) -> etBirhdate.setText(day1 + "/" + (month1 + 1) + "/" + year1),
                    year, month, day);
            datePickerDialog.show();
        });

        return view;
    }
}

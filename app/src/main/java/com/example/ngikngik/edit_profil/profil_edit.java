package com.example.ngikngik.edit_profil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.Dashboard.dashboard;
import com.example.ngikngik.R;

import java.util.ArrayList;
import java.util.Calendar;

public class profil_edit extends Fragment {
    private ImageView imageView;
    private EditText etBirhdate, etNama, etNomorwhatsapp, etNamaorangtua, etAlamat;
    private Spinner spinnerJeniskelamin;
    private RecyclerView recyclerView;
    private ClassAdapter kelasAdapter;
    private EmailAdapter emailAdapter;
    private RecyclerView recyclerViewEmail;
    private ArrayList<item_email> emailList;
    private ArrayList<item_class> kelasList;
    private SharedPreferences sharedPreferences;
    private String selectedGender = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_edit, container, false);
        Log.d("DEBUG", "profil_edit fragment loaded.");

        // Inisialisasi View
        imageView = view.findViewById(R.id.imgBack);
        etNama = view.findViewById(R.id.etnamaprofil);
        spinnerJeniskelamin = view.findViewById(R.id.spGender);
        etNamaorangtua = view.findViewById(R.id.etNamaOrangTua);
        etAlamat = view.findViewById(R.id.etAlamatEdit);
        etBirhdate = view.findViewById(R.id.etBirthDate);
        recyclerView = view.findViewById(R.id.rvKelas);
        recyclerViewEmail = view.findViewById(R.id.rvEmail);
        recyclerViewEmail.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Email tidak ditemukan");
        Log.d("DEBUG", "Email yang diambil dari SharedPreferences: " + email);

        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        emailList = new ArrayList<>();
        emailList.add(new item_email(email));

        kelasList = new ArrayList<>();
        kelasList.add(new item_class(kelas));


        emailAdapter = new EmailAdapter(emailList, emailItem -> {
            Log.d("DEBUG", "email yang dipilih: " + emailItem.getEmailName());
        });


        kelasAdapter = new ClassAdapter(kelasList, classItem -> {
            Log.d("DEBUG", "Kelas yang dipilih: " + classItem.getClassName());
        });

        recyclerViewEmail.setAdapter(emailAdapter);
        recyclerView.setAdapter(kelasAdapter);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), dashboard.class);
            startActivity(intent);
        });

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
        spinnerJeniskelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getItemAtPosition(position).toString();
                Log.d("DEBUG", "Jenis Kelamin yang dipilih: " + selectedGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGender = ""; // Jika tidak ada yang dipilih
            }
        });

        return view;
    }

    // Fungsi validasi input
    public boolean validateFields() {
        boolean isValid = true;

        if (etNama.getText().toString().trim().isEmpty()) {
            etNama.setError("Nama harus diisi!");
            isValid = false;
        }
        if (etBirhdate.getText().toString().trim().isEmpty()) {
            etBirhdate.setError("Tanggal lahir harus diisi!");
            isValid = false;

            if (etAlamat.getText().toString().trim().isEmpty()) {
                etAlamat.setError("Alamat harus diisi!");
                isValid = false;
            }

            if (etNomorwhatsapp.getText().toString().trim().isEmpty()) {
                etNomorwhatsapp.setError("Nomor WhatsApp harus diisi!");
                isValid = false;
            }

            if (etBirhdate.getText().toString().trim().isEmpty()) {
                etBirhdate.setError("Tanggal lahir harus diisi!");
                isValid = false;
            }
            if (etNamaorangtua.getText().toString().trim().isEmpty()) {
                etNamaorangtua.setError("Tanggal lahir harus diisi!");
                isValid = false;
            }
            if (selectedGender.isEmpty()) {
                Toast.makeText(getContext(), "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
}
            return isValid;
        }
}
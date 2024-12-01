package com.example.ngikngik.edit_profil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private EditText etBirthdate, etNama, etNomorWhatsApp, etNamaOrangTua, etAlamat;
    private Spinner spinnerJenisKelamin;
    private RecyclerView recyclerViewKelas, recyclerViewEmail;
    private ClassAdapter kelasAdapter;
    private EmailAdapter emailAdapter;
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
        etBirthdate = view.findViewById(R.id.etBirthDate);
        etNomorWhatsApp = view.findViewById(R.id.etPhoneNumber);
        etNamaOrangTua = view.findViewById(R.id.etNamaOrangTua);
        etAlamat = view.findViewById(R.id.etAlamatEdit);

        spinnerJenisKelamin = view.findViewById(R.id.spGender);
        String[] genderOptions = {"Pilih Jenis Kelamin", "Laki-laki", "Perempuan"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, genderOptions);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisKelamin.setAdapter(genderAdapter);


        recyclerViewKelas = view.findViewById(R.id.rvKelas);
        recyclerViewEmail = view.findViewById(R.id.rvEmail);

        recyclerViewEmail.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewKelas.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Email tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        // Inisialisasi data email dan kelas
        emailList = new ArrayList<>();
        emailList.add(new item_email(email));
        kelasList = new ArrayList<>();
        kelasList.add(new item_class(kelas));

        // Adapter untuk email
        emailAdapter = new EmailAdapter(emailList, emailItem ->
                Log.d("DEBUG", "Email yang dipilih: " + emailItem.getEmailName())
        );

        // Adapter untuk kelas
        kelasAdapter = new ClassAdapter(kelasList, classItem ->
                Log.d("DEBUG", "Kelas yang dipilih: " + classItem.getClassName())
        );

        recyclerViewEmail.setAdapter(emailAdapter);
        recyclerViewKelas.setAdapter(kelasAdapter);

        // Navigasi kembali ke dashboard
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), dashboard.class);
            startActivity(intent);
        });

        // Dialog tanggal lahir
        etBirthdate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (datePicker, year1, month1, day1) ->
                            etBirthdate.setText(day1 + "/" + (month1 + 1) + "/" + year1),
                    year, month, day);
            datePickerDialog.show();
        });

        // Listener spinner jenis kelamin
        spinnerJenisKelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getItemAtPosition(position).toString();
                Log.d("DEBUG", "Jenis Kelamin yang dipilih: " + selectedGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGender = "Pilih Jenis Kelamin"; // Set default value
            }
        });

        // Set OnFocusChangeListener untuk EditText untuk menyembunyikan keyboard saat fokus hilang
        etNama.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        etNomorWhatsApp.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });
        etBirthdate.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });


        etNamaOrangTua.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });
        etAlamat.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });


        return view;
    }

    // Fungsi untuk menyembunyikan keyboard
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    // Fungsi validasi input
    public boolean validateFields() {
        boolean isValid = true;

        if (etNama.getText().toString().trim().isEmpty()) {
            etNama.setError("Nama harus diisi!");
            isValid = false;
        }



        if (etAlamat.getText().toString().trim().isEmpty()) {
            etAlamat.setError("Alamat harus diisi!");
            isValid = false;
        }

        if (etNomorWhatsApp.getText().toString().trim().isEmpty()) {
            etNomorWhatsApp.setError("Nomor WhatsApp harus diisi!");
            isValid = false;
        }

//        String birthdate = etBirthdate.getText().toString().trim();
//        if (birthdate.isEmpty() || birthdate.equals("Pilih Tanggal Lahir")) {  // Tambahkan pengecekan ini
//            etBirthdate.setError("Tanggal lahir harus diisi!");
//            isValid = false;
//        }
        if (etNamaOrangTua.getText().toString().trim().isEmpty()) {
            etNamaOrangTua.setError("Nama orang tua harus diisi!");
            isValid = false;

        }
        if (selectedGender.equals("Pilih Jenis Kelamin")) {
            isValid = false;
        }

        // Sembunyikan keyboard jika validasi selesai
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        }

        return isValid;
    }
}
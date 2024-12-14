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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.Adapter.ClassAdapter;
import com.example.ngikngik.Adapter.EmailAdapter;
import com.example.ngikngik.Dashboard.dashboard;
import com.example.ngikngik.R;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.databinding.FragmentProfilEditBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    private FragmentProfilEditBinding fragmentProfilEditBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_edit, container, false);
        Log.d("DEBUG", "profil_edit fragment loaded.");
//fullscreen fragment
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");

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
                            etBirthdate.setText(year1 + "/" + (month1 + 1) + "/" + day1),
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
        loadProfileData();


        return view;
    }

    public void saveProfileData() {
        Log.d("SAVE_PROFILE", "Memulai proses simpan data...");

        if (validateFields()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_EDIT_PROFIL_URL,
                    response -> {
                        Log.d("SAVE_PROFILE", "Respons server: " + response);
                        Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Log.e("SAVE_PROFILE", "Error saat menyimpan data: " + error.getMessage());
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    String email = sharedPreferences.getString("email", null);
                    if (email != null) {
                        params.put("email", email);  // Gunakan email yang ada di SharedPreferences
                    } else {
                        Log.e("SAVE_PROFILE", "Email tidak ditemukan di SharedPreferences.");
                    }

                    // Mengambil nilai input dan menambahkannya ke map untuk dikirim ke server
                    params.put("nama", etNama.getText().toString().trim());
                    params.put("jenis_kelamin", selectedGender);
                    params.put("nomor_whatsapp", etNomorWhatsApp.getText().toString().trim());
                    params.put("tanggal_lahir", etBirthdate.getText().toString().trim());
                    params.put("nama_orang_tua", etNamaOrangTua.getText().toString().trim());
                    params.put("alamat", etAlamat.getText().toString().trim());

                    // Log semua data yang akan dikirim
                    Log.d("SAVE_PROFILE", "Data yang dikirim: " + params.toString());
                    return params;
                }
            };

            // Kirim request ke server menggunakan Volley
            Volley.newRequestQueue(requireContext()).add(stringRequest);
        } else {
            Log.e("SAVE_PROFILE", "Validasi gagal. Tidak bisa menyimpan data.");
            Toast.makeText(requireContext(), "Silahkan isi kolom yang belum terisi!", Toast.LENGTH_SHORT).show();
        }

        // Simpan data ke SharedPreferences untuk persistensi lokal
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nama", etNama.getText().toString().trim());
        editor.putString("jenis_kelamin", selectedGender);
        editor.putString("nomor_whatsapp", etNomorWhatsApp.getText().toString().trim());
        editor.putString("tanggal_lahir", etBirthdate.getText().toString().trim());
        editor.putString("nama_orang_tua", etNamaOrangTua.getText().toString().trim());
        editor.putString("alamat", etAlamat.getText().toString().trim());

        // Commit perubahan ke SharedPreferences
        editor.apply();

        // Tampilkan pesan jika data berhasil disimpan
        Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
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

        String birthdate = etBirthdate.getText().toString().trim();
        if (birthdate.isEmpty() || birthdate.equals("Pilih Tanggal Lahir")) {  // Tambahkan pengecekan ini
            isValid = false;
        }
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

    private void loadProfileData() {
        Log.d("LOAD_PROFILE", "Memulai proses muat data...");

        // Ambil data dari SharedPreferences
        String nama = sharedPreferences.getString("nama", "");
        String gender = sharedPreferences.getString("jenis_kelamin", "Pilih Jenis Kelamin");
        String nomorWhatsApp = sharedPreferences.getString("nomor_whatsapp", "");
        String tanggalLahir = sharedPreferences.getString("tanggal_lahir", "");
        String namaOrangTua = sharedPreferences.getString("nama_orang_tua", "");
        String alamat = sharedPreferences.getString("alamat", "");

        // Isi data ke elemen UI
        etNama.setText(nama);
        if (!nama.isEmpty()) {
            etNama.setSelection(nama.length()); // Set cursor di akhir teks
        }
        etNomorWhatsApp.setText(nomorWhatsApp);
        etBirthdate.setText(tanggalLahir);
        etNamaOrangTua.setText(namaOrangTua);
        etAlamat.setText(alamat);

        // Atur spinner jenis kelamin
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerJenisKelamin.getAdapter();
        int spinnerPosition = adapter.getPosition(gender);
        spinnerJenisKelamin.setSelection(spinnerPosition);

        Log.d("LOAD_PROFILE", "Data berhasil dimuat.");
    }
}
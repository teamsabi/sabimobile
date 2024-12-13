package com.example.ngikngik.berandaygy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.Adapter.ClassAdapter;
import com.example.ngikngik.Adapter.NameAdapter;
import com.example.ngikngik.R;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.edit_profil.item_class;
import com.example.ngikngik.profil.item_name;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class raport extends Fragment {
    private RecyclerView rvNamaRaport, rvKelasRaport;
    private NameAdapter nameAdapter;
    private ClassAdapter classAdapter;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raport, container, false);
        Log.d("DEBUG", "profil_edit fragment loaded.");
//fullscreen fragment
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Mengambil data nama dan kelas dari SharedPreferences
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        List<item_name> nameList = new ArrayList<>();
        nameList.add(new item_name(nama));

        nameAdapter = new NameAdapter(nameList, nameItem -> {
            Log.d("NameAdapter", "Nama: " + nameItem.getClassName());
        });
        rvNamaRaport.setAdapter(nameAdapter);
        rvNamaRaport.setLayoutManager(new LinearLayoutManager(getContext()));
        List<item_class> classList = new ArrayList<>();
        classList.add(new item_class(kelas));
        classAdapter = new ClassAdapter(classList, classItem -> {
            Log.d("ClassAdapter", "Kelas: " + classItem.getClassName());
        });
        rvKelasRaport.setAdapter(classAdapter);
        rvKelasRaport.setLayoutManager(new LinearLayoutManager(getContext()));


        loadProfileFromServer();
        return view;
    }
    private void loadProfileFromServer() {
        String email = sharedPreferences.getString("email", ""); // Ambil email yang sudah tersimpan
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_NAMA_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if ("success".equals(status)) {
                            String nama = jsonResponse.getString("nama");

                            // Tampilkan nama ke UI
                            List<item_name> nameList = new ArrayList<>();
                            nameList.add(new item_name(nama));
                            nameAdapter.updateData(nameList);

                            // Simpan nama ke SharedPreferences untuk cache lokal
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nama", nama);
                            editor.apply();
                        } else {
                            Log.e("PROFILE_LOAD", "Error: " + jsonResponse.getString("message"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("PROFILE_LOAD", "Error: " + error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); // Kirimkan email ke server untuk mendapatkan nama
                return params;
            }
        };

        // Menambahkan permintaan ke antrian Volley
        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Pastikan data dari SharedPreferences ditampilkan setelah login
        String nama = sharedPreferences.getString("nama", "Nama Default");

        // Perbarui RecyclerView dengan nama baru
        if (nameAdapter != null) {
            List<item_name> nameList = new ArrayList<>();
            nameList.add(new item_name(nama));
            nameAdapter.updateData(nameList);
        }
    }

}
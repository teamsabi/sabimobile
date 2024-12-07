package com.example.ngikngik.Dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.R;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.edit_profil.ClassAdapter;
import com.example.ngikngik.edit_profil.item_class;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class profile_akun extends Fragment {
    private RecyclerView rvNamaAkun, rvKelasAkun;
    private NameAdapter nameAdapter;
    private ClassAdapter classAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_akun, container, false);

        // Inisialisasi RecyclerView
        rvNamaAkun = view.findViewById(R.id.rvNamaAkun);
        rvKelasAkun = view.findViewById(R.id.rvKelasAkun);

        // Inisialisasi SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Mengambil data nama dan kelas dari SharedPreferences
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        // Membuat data untuk NameAdapter
        List<item_name> nameList = new ArrayList<>();
        nameList.add(new item_name(nama));

        // Menambahkan listener pada NameAdapter
        nameAdapter = new NameAdapter(nameList, nameItem -> {
            Log.d("NameAdapter", "Nama: " + nameItem.getClassName());
        });
        rvNamaAkun.setAdapter(nameAdapter);
        rvNamaAkun.setLayoutManager(new LinearLayoutManager(getContext()));

        // Membuat data untuk ClassAdapter
        List<item_class> classList = new ArrayList<>();
        classList.add(new item_class(kelas));
        classAdapter = new ClassAdapter(classList, classItem -> {
            Log.d("ClassAdapter", "Kelas: " + classItem.getClassName());
        });
        rvKelasAkun.setAdapter(classAdapter);
        rvKelasAkun.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void loadProfileFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_NAMA_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String nama = jsonResponse.getString("nama");

                        // Tampilkan nama ke UI
                        nameAdapter.notifyDataSetChanged();

                        // Simpan nama ke SharedPreferences untuk cache lokal
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("nama", nama);
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("PROFILE_LOAD", "Error: " + error.getMessage())
        );
        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sharedPreferences != null) {
            String nama = sharedPreferences.getString("nama", "Default Nama");
            Log.d("onResume", "Nama dari SharedPreferences: " + nama);
        } else {
            Log.e("ERROR", "SharedPreferences belum diinisialisasi.");
        }
    }
}

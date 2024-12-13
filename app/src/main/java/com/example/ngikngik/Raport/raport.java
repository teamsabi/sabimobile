package com.example.ngikngik.Raport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.Adapter.ClassAdapter;
import com.example.ngikngik.Adapter.MapelAdapter;
import com.example.ngikngik.Adapter.NameAdapter;
import com.example.ngikngik.R;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.edit_profil.item_class;
import com.example.ngikngik.profil.item_name;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class raport extends Fragment {
    private RecyclerView rvNamaRaport, rvKelasRaport, rvMapelRaport;
    private NameAdapter nameAdapter;
    private ClassAdapter classAdapter;
    private SharedPreferences sharedPreferences;
    private List<item_mapel> mapelList;
    private MapelAdapter mapelAdapter; // Mengubah nama variabel adapter

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raport   , container, false);

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        // Inisialisasi RecyclerView
        rvNamaRaport = view.findViewById(R.id.rvNamaRaport);
        rvKelasRaport = view.findViewById(R.id.rvKelasRaport);
        rvMapelRaport = view.findViewById(R.id.rvMapelRaport);

        // Inisialisasi Adapter untuk RecyclerView Nama
        List<item_name> nameList = new ArrayList<>();
        nameList.add(new item_name(nama));
        nameAdapter = new NameAdapter(nameList, nameItem -> {
            Log.d("NameAdapter", "Nama: " + nameItem.getClassName());
        });
        rvNamaRaport.setAdapter(nameAdapter);
        rvNamaRaport.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi Adapter untuk RecyclerView Kelas
        List<item_class> classList = new ArrayList<>();
        classList.add(new item_class(kelas));
        classAdapter = new ClassAdapter(classList, classItem -> {
            Log.d("ClassAdapter", "Kelas: " + classItem.getClassName());
        });
        rvKelasRaport.setAdapter(classAdapter);
        rvKelasRaport.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi RecyclerView Mapel
        rvMapelRaport.setLayoutManager(new LinearLayoutManager(getContext()));

        // Panggil LoadMapel setelah RecyclerView siap
        LoadMapel(kelas); // Pastikan LoadMapel dipanggil dengan kelas yang benar

        return view;
    }

    // Memuat data mata pelajaran berdasarkan kelas
    private void LoadMapel(String kelas) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.POST, DbContract.SERVER_MAPEL_URL,
                response -> {
                    try {
                        JSONArray jadwalArray = new JSONArray(response);
                        mapelList = new ArrayList<>();

                        for (int i = 0; i < jadwalArray.length(); i++) {
                            JSONObject obj = jadwalArray.getJSONObject(i);
                            String matkul = obj.getString("mata_pelajaran");
                            mapelList.add(new item_mapel(matkul));
                        }

                        // Set data ke RecyclerView hanya setelah data berhasil dimuat
                        mapelAdapter = new MapelAdapter(mapelList);
                        rvMapelRaport.setAdapter(mapelAdapter); // Pasang adapter setelah data berhasil dimuat

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kelas", kelas); // Kirim kelas user
                return params;
            }
        };

        // Menambahkan permintaan ke antrian Volley
        queue.add(request);
    }

    // Memuat profile user dari server
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

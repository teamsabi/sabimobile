package com.example.ngikngik.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.edit_profil.ClassAdapter;
import com.example.ngikngik.edit_profil.item_class;
import com.example.ngikngik.edit_profil.simpan;
import com.example.ngikngik.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class profile_akun extends Fragment {
    private RecyclerView rvNamaAkun, rvKelasAkun;
    private NameAdapter nameAdapter;
    private ClassAdapter classAdapter;
    private SharedPreferences sharedPreferences;
    private ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil_akun, container, false);


        rvNamaAkun = view.findViewById(R.id.rvNamaAkun);
        rvKelasAkun = view.findViewById(R.id.rvKelasAkun);


        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Mengambil data nama dan kelas dari SharedPreferences
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        List<item_name> nameList = new ArrayList<>();
        nameList.add(new item_name(nama));


        nameAdapter = new NameAdapter(nameList, nameItem -> {
            Log.d("NameAdapter", "Nama: " + nameItem.getClassName());
        });
        rvNamaAkun.setAdapter(nameAdapter);
        rvNamaAkun.setLayoutManager(new LinearLayoutManager(getContext()));
        List<item_class> classList = new ArrayList<>();
        classList.add(new item_class(kelas));
        classAdapter = new ClassAdapter(classList, classItem -> {
            Log.d("ClassAdapter", "Kelas: " + classItem.getClassName());
        });
        rvKelasAkun.setAdapter(classAdapter);
        rvKelasAkun.setLayoutManager(new LinearLayoutManager(getContext()));

        // Memanggil fungsi untuk load data profil dari server

        // Menghilangkan status bar
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        );
        imageView = view.findViewById(R.id.btnEdit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), simpan.class);
                startActivity(intent);
            }
        });
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

        // Memastikan data yang ada di SharedPreferences tetap ditampilkan
        String nama = sharedPreferences.getString("nama", "Default Nama");
        Log.d("onResume", "Nama dari SharedPreferences: " + nama);

        // Memperbarui UI jika ada perubahan
        if (nameAdapter != null) {
            List<item_name> nameList = new ArrayList<>();
            nameList.add(new item_name(nama));
            nameAdapter.updateData(nameList);
        }
    }
}
package com.example.ngikngik.berandaygy;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
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
import com.example.ngikngik.Adapter.JadwalAdapter;
import com.example.ngikngik.Adapter.NameAdapter;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class beranda extends Fragment {
    private RecyclerView recyclerView;
    private JadwalAdapter adapter;
    private List<item_Jadwal> jadwalList;
    private NameAdapter nameAdapter;
    private ClassAdapter classAdapter;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        //fullscreen fragment
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView tvNamaBeranda = view.findViewById(R.id.tvNamaBeranda);

        // Inisialisasi sharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Load data kelas dan email dari SharedPreferences
        String kelas = sharedPreferences.getString("kelas", "kelas tidak ditemukan"); // Ambil data kelas dari SharedPreferences
        String email = sharedPreferences.getString("email", "Email tidak ditemukan");
        String nama = sharedPreferences.getString("nama", ""); // Dapatkan nama, jika kosong berarti pengguna baru

        // Cek jika nama kosong, artinya pengguna baru
        if (nama.isEmpty()) {
            tvNamaBeranda.setText("Halo, pengguna baru!"); // Pesan untuk pengguna baru
            loadProfileFromServer(email, tvNamaBeranda); // Ambil nama dari server
        } else {
            tvNamaBeranda.setText("Halo, " + nama + "!"); // Pesan untuk pengguna yang sudah memiliki nama
        }

        loadJadwal(kelas); // Memuat jadwal

        return view;
    }

    private void loadJadwal(String kelas) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.POST, DbContract.SERVER_JADWAL_URL,
                response -> {
                    try {
                        JSONArray jadwalArray = new JSONArray(response);
                        jadwalList = new ArrayList<>();

                        for (int i = 0; i < jadwalArray.length(); i++) {
                            JSONObject obj = jadwalArray.getJSONObject(i);
                            String hari = obj.getString("hari");
                            String matkul = obj.getString("mata_pelajaran");

                            jadwalList.add(new item_Jadwal(hari, matkul));
                        }

                        // Set data ke RecyclerView
                        adapter = new JadwalAdapter(jadwalList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kelas", kelas); // Kirim kelas user
                return params;
            }
        };

        queue.add(request);
    }

    private void loadProfileFromServer(String email, TextView tvNamaBeranda) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_NAMA_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if ("success".equals(status)) {
                            String nama = jsonResponse.getString("nama");

                            // Tampilkan nama ke UI
                            tvNamaBeranda.setText("Halo, " + nama + "!");

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
}

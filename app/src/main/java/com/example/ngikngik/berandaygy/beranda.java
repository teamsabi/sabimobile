package com.example.ngikngik.berandaygy;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.example.ngikngik.Adapter.JadwalAdapter;
import com.example.ngikngik.Adapter.NamaAdapter;
import com.example.ngikngik.R;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.berandaygy.materi.materi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class beranda extends Fragment {
    private RecyclerView recyclerView;
    private JadwalAdapter adapter;
    private List<item_Jadwal> jadwalList;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "BerandaFragment";
    private RecyclerView rvNamaBeranda;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        // Inisialisasi sharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "Nama Default");

        // Ambil ID user dari SharedPreferences
        String userId = sharedPreferences.getString("id_user", "");
        if (userId.isEmpty()) {
            Log.e(TAG, "Error: id_user tidak ditemukan di SharedPreferences.");
            Toast.makeText(getContext(), "ID User tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            return view; // Berhenti jika id_user tidak ditemukan
        }

        // Fullscreen fragment
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

        // Menampilkan nama di RecyclerView menggunakan TextView
        rvNamaBeranda = view.findViewById(R.id.rvNamaBeranda);
        rvNamaBeranda.setLayoutManager(new LinearLayoutManager(getContext()));

        // Membuat list data (misalnya list nama pengguna)
        List<String> namaList = new ArrayList<>();
        namaList.add(nama); // Menambahkan nama pengguna yang sudah disimpan di SharedPreferences ke dalam list

        // Membuat adapter untuk menampilkan nama di RecyclerView
        NamaAdapter namaAdapter = new NamaAdapter(namaList);
        rvNamaBeranda.setAdapter(namaAdapter);

        // Klik gambar materi
        ImageView viewMateri = view.findViewById(R.id.ImgMateri);
        viewMateri.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), materi.class);
            startActivity(intent);
        });

        // Load jadwal berdasarkan id_user
        loadJadwal(userId);

        return view;
    }

    private void loadJadwal(String id_user) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = DbContract.SERVER_JADWAL_URL + "?id_user=" + id_user;

        // Log URL untuk debug
        Log.d(TAG, "Request URL: " + url);

        // Membuat permintaan GET
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("message")) {
                            String message = jsonResponse.getString("message");
                            Log.e(TAG, "Error from server: " + message);
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray jadwalArray = jsonResponse.getJSONArray("data");
                        jadwalList = new ArrayList<>();

                        for (int i = 0; i < jadwalArray.length(); i++) {
                            JSONObject obj = jadwalArray.getJSONObject(i);
                            String tanggal = obj.getString("tanggal");
                            String namaKelas = obj.getString("nama_kelas");
                            String namaMapel = obj.getString("nama_mapel");
                            String namaLengkap = obj.getString("nama_lengkap");

                            item_Jadwal item = new item_Jadwal(tanggal, namaKelas, namaMapel, namaLengkap);
                            jadwalList.add(item);
                        }

                        if (jadwalList.isEmpty()) {
                            Toast.makeText(requireContext(), "Tidak ada jadwal tersedia.", Toast.LENGTH_SHORT).show();
                        }

                        adapter = new JadwalAdapter(jadwalList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing data: " + e.getMessage());
                        Toast.makeText(requireContext(), "Error parsing data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error: " + error.getMessage());
                    Toast.makeText(requireContext(), "Gagal memuat jadwal. Periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}

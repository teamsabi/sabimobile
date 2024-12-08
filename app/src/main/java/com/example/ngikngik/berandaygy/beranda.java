package com.example.ngikngik.berandaygy;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.ngikngik.Adapter.JadwalAdapter;
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
    private List<Jadwal> jadwalList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load data jadwal berdasarkan kelas
        SharedPreferences preferences = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String kelas = preferences.getString("kelas", "kelas tidak ditemukan"); // Ambil data kelas dari SharedPreferences
        String email = preferences.getString("email", "Email tidak ditemukan");
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

                            jadwalList.add(new Jadwal(hari, matkul));
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
}

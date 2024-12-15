package com.example.ngikngik.berandaygy.materi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.Adapter.MapelAdapter;
import com.example.ngikngik.Adapter.MapelMateriAdapter;
import com.example.ngikngik.R;
import com.example.ngikngik.Raport.item_mapel;
import com.example.ngikngik.api.DbContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class materi extends AppCompatActivity {
    private TextView tvNamaMateri; // TextView untuk menampilkan nama
    private RecyclerView rvMateri; // RecyclerView untuk menampilkan daftar jadwal atau materi
    private SharedPreferences sharedPreferences;
    private List<item_mapelmateri> mapelmateriList; // Daftar data jadwal atau materi
    private MapelMateriAdapter materimapelAdapter; // Adapter untuk RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materi);

        // Mengatur padding sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        // Inisialisasi TextView dan RecyclerView
        tvNamaMateri = findViewById(R.id.tvNamaMateri);
        rvMateri = findViewById(R.id.rvMapelMateri);

        // Tampilkan nama pengguna di TextView
        tvNamaMateri.setText("Halo, " + nama);

        // Atur RecyclerView untuk daftar materi atau jadwal
        rvMateri.setLayoutManager(new LinearLayoutManager(this));
        LoadMateri(kelas); // Panggil fungsi untuk memuat data berdasarkan kelas
    }

    // Fungsi untuk memuat daftar materi atau jadwal
    private void LoadMateri(String kelas) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, DbContract.SERVER_MAPEL_URL,
                response -> {
                    try {
                        JSONArray jadwalArray = new JSONArray(response);
                        mapelmateriList = new ArrayList<>();

                        for (int i = 0; i < jadwalArray.length(); i++) {
                            JSONObject obj = jadwalArray.getJSONObject(i);
                            String matkul = obj.getString("mata_pelajaran");
                            mapelmateriList.add(new item_mapelmateri(matkul)); // Gunakan item_mapelmateri
                        }

                        // Pasang data ke RecyclerView melalui adapter
                        materimapelAdapter = new MapelMateriAdapter(mapelmateriList); // Gunakan mapelmateriList
                        rvMateri.setAdapter(materimapelAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kelas", kelas); // Kirim kelas user ke server
                return params;
            }
        };

        // Tambahkan permintaan ke antrian
        queue.add(request);
    }
}


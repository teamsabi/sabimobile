package com.example.ngikngik.berandaygy.materi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailMateriActivity extends AppCompatActivity {

    private ListView listViewMateri;
    private MateriAdapter materiAdapter;
    private ImageView imageView;
    private ArrayList<Materid> materiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_materi);

        listViewMateri = findViewById(R.id.listViewMateri);
        materiList = new ArrayList<>();

        imageView = findViewById(R.id.imgBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailMateriActivity.this, materi.class);
                startActivity(intent);
            }
        });
        // Mengambil kode_mapel dari Intent
        Intent intent = getIntent();
        String kodeMapel = intent.getStringExtra("kode_mapel");

        // Fetch materi data berdasarkan kode_mapel
        fetchMateriData(kodeMapel);
    }

    private void fetchMateriData(String kodeMapel) {
        String url = "http://192.168.1.12/api/materi.php?kode_mapel=" + kodeMapel; // Ganti dengan URL API Anda

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray materiArray = jsonResponse.getJSONArray("materi");
                            for (int i = 0; i < materiArray.length(); i++) {
                                JSONObject materiObject = materiArray.getJSONObject(i);

                                String judulMateri = materiObject.getString("judul_materi");
                                String fileMateri = materiObject.getString("file_materi");

                                // Menambahkan materi ke list
                                materiList.add(new Materid(judulMateri, fileMateri));
                            }

                            // Menyusun data ke dalam ListView
                            materiAdapter = new MateriAdapter(DetailMateriActivity.this, materiList);
                            listViewMateri.setAdapter(materiAdapter);

                        } else {
                            Toast.makeText(DetailMateriActivity.this, "Materi tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailMateriActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(DetailMateriActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                });

        // Add request to Volley queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}

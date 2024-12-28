package com.example.ngikngik.berandaygy.materi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.R;
import com.example.ngikngik.Dashboard.dashboard;
import com.example.ngikngik.berandaygy.materi.Matematika.judul_Matematika;
import com.example.ngikngik.berandaygy.materi.fisika.fisika;
import com.example.ngikngik.berandaygy.materi.kimia.kimia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class materi extends AppCompatActivity {
    private TextView tvNamaMateri;
    private LinearLayout linearLayoutMapel;
    private SharedPreferences sharedPreferences;
    private ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");

        // Initialize views
        tvNamaMateri = findViewById(R.id.tvNamaMateri);
        linearLayoutMapel = findViewById(R.id.linearLayoutMapel);

        imgback = findViewById(R.id.imgBack);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(materi.this, dashboard.class);
                startActivity(intent);
            }
        });

        // Set welcome message
        tvNamaMateri.setText("Selamat datang, " + nama);

        // Get id_user from SharedPreferences
        String id_user = sharedPreferences.getString("id_user", "0");

        // Fetch mata pelajaran data
        fetchMapelData(id_user);
    }

    private void fetchMapelData(String id_user) {
        String url = "http://192.168.1.4/api/JustMapel.php?id_user=" + id_user; // Ganti dengan URL API Anda
        Log.d("materi", "Request URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("materi", "Response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("data")) {
                            JSONArray mapelsArray = jsonResponse.getJSONArray("data");

                            for (int i = 0; i < mapelsArray.length(); i++) {
                                JSONObject mapelObject = mapelsArray.getJSONObject(i);
                                String mapelName = mapelObject.getString("nama_mapel");
                                String kodeMapel = mapelObject.getString("kode_mapel"); // Ambil kode_mapel

                                // Create dynamic CardView
                                createCardView(mapelName, kodeMapel); // Pass kodeMapel as well
                            }
                        } else {
                            Toast.makeText(materi.this, "Tidak ada data mapel", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(materi.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("materi", "Error fetching data", error);
                    Toast.makeText(materi.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                });

        // Add request to Volley queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    /**
     * Membuat CardView secara dinamis untuk setiap mata pelajaran
     *
     * @param mapelName Nama mata pelajaran
     * @param kodeMapel Kode mata pelajaran
     */
    private void createCardView(String mapelName, String kodeMapel) {
        // Create CardView
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardLayoutParams.setMargins(16, 8, 16, 8); // Margin untuk tiap CardView
        cardView.setLayoutParams(cardLayoutParams);
        cardView.setRadius(16);
        cardView.setElevation(8);
        cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        cardView.setUseCompatPadding(true);

        // Create LinearLayout inside CardView
        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.HORIZONTAL);
        cardContent.setPadding(16, 16, 16, 16);

        // Add Icon ImageView
        ImageView mapelIcon = new ImageView(this);
        mapelIcon.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        mapelIcon.setImageResource(R.drawable.baseline_book_24); // Ganti dengan ikon spesifik jika ada
        mapelIcon.setPadding(8, 8, 8, 8);

        // Create TextView for mapel name
        TextView mapelTextView = new TextView(this);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textLayoutParams.setMargins(16, 0, 0, 0); // Jarak antara ikon dan teks
        mapelTextView.setLayoutParams(textLayoutParams);
        mapelTextView.setText(mapelName);
        mapelTextView.setTextSize(18);
        mapelTextView.setTextColor(getResources().getColor(android.R.color.black));
        mapelTextView.setTypeface(null, Typeface.BOLD);

        // Create TextView for kode mapel
        TextView kodeMapelTextView = new TextView(this);
        kodeMapelTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        kodeMapelTextView.setText("Kode Mapel: " + kodeMapel);
        kodeMapelTextView.setTextSize(14);
        kodeMapelTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));

        // Add views to CardView
        cardContent.addView(mapelIcon);
        cardContent.addView(mapelTextView);
        cardContent.addView(kodeMapelTextView); // Add kodeMapelTextView here
        cardView.addView(cardContent);

        // Set click listener
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(materi.this, DetailMateriActivity.class);
            intent.putExtra("kode_mapel", kodeMapel);  // Mengirimkan kode_mapel ke Activity detail materi
            startActivity(intent);
        });

        // Add CardView to parent layout
        linearLayoutMapel.addView(cardView);
    }
}

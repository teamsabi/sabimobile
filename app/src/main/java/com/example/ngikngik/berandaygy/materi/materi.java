package com.example.ngikngik.berandaygy.materi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.Adapter.MapelMateriAdapter;
import com.example.ngikngik.Dashboard.dashboard;
import com.example.ngikngik.R;

import com.example.ngikngik.berandaygy.materi.Matematika.judul_Matematika;
import com.example.ngikngik.databinding.ActivityDashboardBinding;

import java.util.List;

public class materi extends AppCompatActivity {
    private TextView tvNamaMateri, txt_materi; // TextView untuk menampilkan nama
    private RecyclerView rvMateri; // RecyclerView untuk menampilkan daftar jadwal atau materi
    private SharedPreferences sharedPreferences;
    private List<item_mapelmateri> mapelmateriList; // Daftar data jadwal atau materi
    private MapelMateriAdapter materimapelAdapter; // Adapter untuk RecyclerView
    private ActivityDashboardBinding binding;
    private ImageView imgback;
    private LinearLayout linearMatematika,linearkimia, linearfisika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materi);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Setel padding hanya untuk system bars satu kali
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        linearMatematika = findViewById(R.id.linearLayoutMatematika);
        linearMatematika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (materi.this, judul_Matematika.class);
                startActivity(intent);
            }
        });
        linearMatematika = findViewById(R.id.linearLayoutKimia);
        linearMatematika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (materi.this, judul_Matematika.class);
                startActivity(intent);
            }
        });

        linearMatematika = findViewById(R.id.linearLayoutFisika);
        linearMatematika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (materi.this, judul_Matematika.class);
                startActivity(intent);
            }
        });


        imgback = findViewById(R.id.img_materi);
        imgback.setOnClickListener(v -> {
            Intent intent = new Intent(materi.this, dashboard.class);
            startActivity(intent);
        });

        txt_materi = findViewById(R.id.txt_materi);
        txt_materi.setOnClickListener(v -> {
            Intent intent = new Intent(materi.this, dashboard.class);
            startActivity(intent);
        });

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");
        String kelas = sharedPreferences.getString("kelas", "Kelas tidak ditemukan");

        // Inisialisasi TextView dan RecyclerView
        tvNamaMateri = findViewById(R.id.tvNamaMateri);

        // Tampilkan nama pengguna di TextView
        tvNamaMateri.setText("Halo, " + nama);
    }
}
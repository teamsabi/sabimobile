package com.example.ngikngik.berandaygy.materi.fisika;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ngikngik.R;
import com.example.ngikngik.berandaygy.materi.materi;

public class fisika extends AppCompatActivity {
    private LinearLayout btnbackFisika;
    private SharedPreferences sharedPreferences;
    private TextView tvNamaMateri, txt_materi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_judul_matematika);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");

            tvNamaMateri = findViewById(R.id.tvNamaMateri);

            tvNamaMateri.setText("Halo, " + nama);

            btnbackFisika = findViewById(R.id.btnBackMTK);
            btnbackFisika.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (fisika.this, materi.class);
                    startActivity(intent);
                }
            });

            return insets;
        });
    }
}
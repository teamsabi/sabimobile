package com.example.ngikngik.berandaygy.materi.fisika;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.example.ngikngik.R;
import com.example.ngikngik.berandaygy.materi.kimia.kimia;
import com.example.ngikngik.berandaygy.materi.materi;

public class fisika extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView tvNamaMateri, txt_materi; // TextView untuk menampilkan nama
    private LinearLayout btnbackfisika;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fisika);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String nama = sharedPreferences.getString("nama", "Nama tidak ditemukan");

            ImageView imageViewDownload1 = findViewById(R.id.downloadfisikadasar);
            ImageView imageViewDownload2 = findViewById(R.id.downloadatom);
            ImageView imageViewDownload3 = findViewById(R.id.downloadsifatunsur);

            String url1 = "https://wstif23.myhost.id/kelas_b/team_5/admin/file/Kelas 12 Fisika.pdf";
            String url2 = "https://wstif23.myhost.id/kelas_b/team_5/admin/file/Model Atom.pdf";
            String url3 = "https://wstif23.myhost.id/kelas_b/team_5/admin/file/Sifat Periodik Unsur.pdf";


            imageViewDownload1.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                startActivity(browserIntent);
            });

            imageViewDownload2.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
                startActivity(browserIntent);
            });

            imageViewDownload3.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url3));
                startActivity(browserIntent);
            });

            tvNamaMateri = findViewById(R.id.tvNamaMateri);

            tvNamaMateri.setText("Halo, " + nama);
            btnbackfisika = findViewById(R.id.btnBackFisika);
            btnbackfisika.setOnClickListener(new View.OnClickListener() {
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
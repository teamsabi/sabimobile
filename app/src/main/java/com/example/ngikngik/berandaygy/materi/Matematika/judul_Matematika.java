package com.example.ngikngik.berandaygy.materi.Matematika;

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
import com.example.ngikngik.berandaygy.materi.materi;

public class judul_Matematika extends AppCompatActivity {
private LinearLayout btnbackMTK;
    private SharedPreferences sharedPreferences;
    private TextView tvNamaMateri, txt_materi; // TextView untuk menampilkan nama

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
            ImageView imageViewDownload1 = findViewById(R.id.imageView29);
            ImageView imageViewDownload2 = findViewById(R.id.imageView34);
            ImageView imageViewDownload3 = findViewById(R.id.imageView31);

            String url1 = "https://wstif23.myhost.id/kelas_b/team_5/admin/file/Buku Ajar Matematika Dasar.pdf";
            String url2 = "https://wstif23.myhost.id/kelas_b/team_5/admin/file/Matematika Diskrit.pdf";
            String url3 = "https://wstif23.myhost.id/kelas_b/team_5/admin/file/Aljabar.pdf";

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

            btnbackMTK = findViewById(R.id.btnBackMTK);
            btnbackMTK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (judul_Matematika.this, materi.class);
                    startActivity(intent);
                }
            });

            return insets;
        });
    }
}
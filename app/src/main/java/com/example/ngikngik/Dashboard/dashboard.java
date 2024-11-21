package com.example.ngikngik.Dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ngikngik.R;

public class dashboard extends AppCompatActivity {
    FrameLayout fragmentcontainer;
    LinearLayout berandadaftar;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        fragmentcontainer = (FrameLayout) findViewById(R.id.fragment_container);
        berandadaftar = (LinearLayout) findViewById(R.id.linearLayout2);

        berandadaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                switch (;)
            }
        });
        }
    }

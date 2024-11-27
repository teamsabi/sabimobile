package com.example.ngikngik.Dashboard.edit_profil;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ngikngik.Dashboard.Jadwal;
import com.example.ngikngik.Dashboard.JadwalAdapter;
import com.example.ngikngik.Dashboard.ProfilDashboard;
import com.example.ngikngik.R;

import java.util.Arrays;
import java.util.List;

public class profil_edit extends Fragment {
    private ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_edit, container, false);
        Log.d("DEBUG", "profil_edit fragment loaded.");

        imageView = view.findViewById(R.id.imgBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfilDashboard.class);
                startActivity(intent);
            }
        });
        return view;


    }
  }

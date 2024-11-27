package com.example.ngikngik.Dashboard.edit_profil;

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

import com.example.ngikngik.Dashboard.Jadwal;
import com.example.ngikngik.Dashboard.JadwalAdapter;
import com.example.ngikngik.R;

import java.util.Arrays;
import java.util.List;

public class profil_edit extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "profil_edit fragment loaded.");
        return inflater.inflate(R.layout.fragment_profil_edit, container, false);

    }
  }

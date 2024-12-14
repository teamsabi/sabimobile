package com.example.ngikngik.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ngikngik.R;
import com.example.ngikngik.berandaygy.item_Jadwal;

import java.util.List;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolder> {

    private List<item_Jadwal> jadwalList;

    public JadwalAdapter(List<item_Jadwal> jadwalList) {
        this.jadwalList = jadwalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jadwal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item_Jadwal jadwal = jadwalList.get(position);
        holder.tvHari.setText(jadwal.getHari());
        holder.tvMatkul.setText(jadwal.getMataPelajaran());
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHari, tvMatkul;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHari = itemView.findViewById(R.id.tvHari);
            tvMatkul = itemView.findViewById(R.id.tvMatkul);
        }
    }
}

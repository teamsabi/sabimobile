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

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder> {
    private List<item_Jadwal> jadwalList;

    public JadwalAdapter(List<item_Jadwal> jadwalList) {
        this.jadwalList = jadwalList;
    }

    @NonNull
    @Override
    public JadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jadwal, parent, false);
        return new JadwalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalViewHolder holder, int position) {
        item_Jadwal jadwal = jadwalList.get(position);
        holder.tvTanggal.setText(jadwal.getTanggal());
        holder.tvNamaKelas.setText(jadwal.getNamaKelas());
        holder.tvNamaMapel.setText(jadwal.getNamaMapel());
        holder.tvNamaLengkap.setText(jadwal.getNamaLengkap());
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public static class JadwalViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvNamaKelas, tvNamaMapel, tvNamaLengkap;

        public JadwalViewHolder(View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvNamaKelas = itemView.findViewById(R.id.tvNamaKelas);
            tvNamaMapel = itemView.findViewById(R.id.tvNamaMapel);
            tvNamaLengkap = itemView.findViewById(R.id.tvNamaLengkap);
        }
    }
}

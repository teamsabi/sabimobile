package com.example.ngikngik.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.R;
import com.example.ngikngik.Raport.item_mapel;
import com.example.ngikngik.berandaygy.materi.item_mapelmateri;

import java.util.List;

public class MapelMateriAdapter extends RecyclerView.Adapter<MapelMateriAdapter.ViewHolder> {

    private List<item_mapelmateri> mapelmateriList;

    public MapelMateriAdapter(List<item_mapelmateri> mapelmateriList) {
        this.mapelmateriList = mapelmateriList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menyiapkan layout item_mapel untuk setiap item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mapelmateri, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item_mapelmateri mapel = mapelmateriList.get(position);

        // Set mata pelajaran pada TextView
        holder.tvMapelMateri.setText(mapel.getMataPelajaran());

        // Set icon panah pada ImageView (misalnya)
        holder.imageView28.setImageResource(R.drawable.baseline_arrow_forward_ios_24); // Gambar panah
    }

    @Override
    public int getItemCount() {
        return mapelmateriList.size();
    }

    // ViewHolder untuk item_mapel
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMapelMateri;
        ImageView imageView28;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMapelMateri = itemView.findViewById(R.id.tvMapelMateri);
            imageView28 = itemView.findViewById(R.id.imageView28);
        }
    }
}


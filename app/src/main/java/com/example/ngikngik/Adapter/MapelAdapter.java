package com.example.ngikngik.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.R;
import com.example.ngikngik.Raport.item_mapel;

import java.util.List;

public class MapelAdapter extends RecyclerView.Adapter<MapelAdapter.ViewHolder> {

    private List<item_mapel> mapelList;

    public MapelAdapter(List<item_mapel> mapelList) {
        this.mapelList = mapelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mapel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item_mapel mapel = mapelList.get(position);
        holder.tvMapel.setText(mapel.getMataPelajaran());
    }

    @Override
    public int getItemCount() {
        return mapelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMapel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMapel = itemView.findViewById(R.id.tvMapel);
        }
    }
}

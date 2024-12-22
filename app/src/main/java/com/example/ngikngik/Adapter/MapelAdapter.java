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

    private final List<item_mapel> mapelList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(item_mapel mapel);
    }

    public MapelAdapter(List<item_mapel> mapelList, OnItemClickListener listener) {
        this.mapelList = mapelList;
        this.listener = listener;
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
        holder.itemView.setOnClickListener(v -> listener.onItemClick(mapel));
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

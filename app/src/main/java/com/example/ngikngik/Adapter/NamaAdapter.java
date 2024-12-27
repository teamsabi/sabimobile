package com.example.ngikngik.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.R;

import java.util.List;

public class NamaAdapter extends RecyclerView.Adapter<NamaAdapter.NamaViewHolder> {
    private List<String> namaList;

    public NamaAdapter(List<String> namaList) {
        this.namaList = namaList;
    }

    @NonNull
    @Override
    public NamaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_name, parent, false);
        return new NamaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NamaViewHolder holder, int position) {
        holder.namaTextView.setText(namaList.get(position));
    }

    @Override
    public int getItemCount() {
        return namaList.size();
    }

    public static class NamaViewHolder extends RecyclerView.ViewHolder {
        TextView namaTextView;

        public NamaViewHolder(View itemView) {
            super(itemView);
            namaTextView = itemView.findViewById(R.id.nameTextView); // Ganti dengan ID yang sesuai
        }
    }
}

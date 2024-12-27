package com.example.ngikngik.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.R;
import com.example.ngikngik.edit_profil.item_class;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private List<item_class> classList;
    private OnClassClickListener onClassClickListener;

    public ClassAdapter(List<item_class> classList, OnClassClickListener onClassClickListener) {
        this.classList = classList;
        this.onClassClickListener = onClassClickListener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        item_class item = classList.get(position);
        holder.classTextView.setText(item.getClassName());
        holder.itemView.setOnClickListener(v -> onClassClickListener.onClassClick(item));
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void updateData(List<item_class> newClassList) {
        this.classList = newClassList;
        notifyDataSetChanged();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classTextView;

        public ClassViewHolder(View itemView) {
            super(itemView);
            classTextView = itemView.findViewById(R.id.classTextView);
        }
    }

    public interface OnClassClickListener {
        void onClassClick(item_class classItem);
    }
}

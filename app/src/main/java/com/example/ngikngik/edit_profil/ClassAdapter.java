package com.example.ngikngik.edit_profil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private List<item_class> classList;
    private OnItemClickListener listener;

    // Constructor
    public ClassAdapter(List<item_class> classList, OnItemClickListener listener) {
        this.classList = classList;
        this.listener = listener;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        item_class currentClass = classList.get(position);
        holder.classNameTextView.setText(currentClass.getClassName());

        // Set click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(currentClass));
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {
        private TextView classNameTextView;

        public ClassViewHolder(View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.getclass);
        }
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(item_class classItem);
    }
}

package com.example.ngikngik.Dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.Dashboard.item_name;
import com.example.ngikngik.R;

import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.NameViewHolder> {
    private List<item_name> nameList;
    private OnItemClickListener listener;

    // Constructor
    public NameAdapter(List<item_name> nameList, OnItemClickListener listener) {
        this.nameList = nameList;
        this.listener = listener;
    }

    @Override
    public NameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_name, parent, false);
        return new NameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NameViewHolder holder, int position) {
        item_name currentName = nameList.get(position);
        holder.nameTextView.setText(currentName.getName());

        // Set click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(currentName));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class NameViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public NameViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.getnama);
        }
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(item_name nameItem);
    }
}

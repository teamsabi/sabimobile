package com.example.ngikngik.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngikngik.R;
import com.example.ngikngik.profil.item_name;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of names.
 */
public class NameAdapter extends RecyclerView.Adapter<NameAdapter.NameViewHolder> {
    private List<item_name> nameList;
    private OnItemClickListener listener;

    /**
     * Constructor for NameAdapter.
     *
     * @param nameList List of item_name objects to display.
     * @param listener Listener for item click events.
     */
    public NameAdapter(List<item_name> nameList, OnItemClickListener listener) {
        this.nameList = nameList != null ? nameList : new ArrayList<>();
        this.listener = listener;
    }

    public NameAdapter(List<item_name> nameList) {
        this(nameList, null);
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_name, parent, false);
        return new NameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        item_name item = nameList.get(position);
        holder.nameTextView.setText(item.getName());

        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public void updateData(List<item_name> newNameList) {
        if (newNameList != null) {
            this.nameList.clear();
            this.nameList.addAll(newNameList);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(item_name nameItem);
    }

    public static class NameViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public NameViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}

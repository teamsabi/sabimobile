package com.example.ngikngik.edit_profil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.ngikngik.R;
import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {
    private List<item_email> emailList;
    private OnItemClickListener emaillistener;

    // Constructor
    public EmailAdapter(List<item_email> emailList, OnItemClickListener emaillistener) {
        this.emailList = emailList;
        this.emaillistener = emaillistener;
    }

    @Override
    public EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_email, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmailViewHolder holder, int position) {
        item_email currentEmail = emailList.get(position);
        holder.emailNameText.setText(currentEmail.getEmailName());

        // Set click emaillistener
        holder.itemView.setOnClickListener(v -> emaillistener.onItemClick(currentEmail));
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public class EmailViewHolder extends RecyclerView.ViewHolder {
        private TextView emailNameText;

        public EmailViewHolder(View itemView) {
            super(itemView);
            emailNameText = itemView.findViewById(R.id.getemail); // Pastikan ID ini sesuai dengan XML
        }
    }

    // Interface for item click emaillistener
    public interface OnItemClickListener {
        void onItemClick(item_email emailItem);
    }
}

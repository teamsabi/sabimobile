package com.example.ngikngik.berandaygy.materi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ngikngik.R;

import java.util.ArrayList;

public class MateriAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Materid> materiList;

    public MateriAdapter(Context context, ArrayList<Materid> materiList) {
        this.context = context;
        this.materiList = materiList;
    }

    @Override
    public int getCount() {
        return materiList.size();
    }

    @Override
    public Object getItem(int position) {
        return materiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_materi, parent, false);
        }

        TextView judulMateriTextView = convertView.findViewById(R.id.tvJudulMateri);
        TextView fileMateriTextView = convertView.findViewById(R.id.tvFileMateri);

        Materid materi = materiList.get(position);

        judulMateriTextView.setText(materi.getJudulMateri());
        fileMateriTextView.setText(materi.getFileMateri());

        return convertView;
    }
}


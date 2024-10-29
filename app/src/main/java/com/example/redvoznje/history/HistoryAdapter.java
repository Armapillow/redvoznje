package com.example.redvoznje.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.redvoznje.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HistoryAdapter extends ArrayAdapter<HistoryEntry> {

    private Context context;
    private int resource;
    private ArrayList<HistoryEntry> history;

    public HistoryAdapter(@NonNull Context context, int resource, ArrayList<HistoryEntry> history) {
        super(context, resource, history);

        this.context = context;
        this.resource = resource;
        this.history = history;
        Collections.reverse(history);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String stationFrom = getItem(position).getStationFrom();
        String stationTo = getItem(position).getStationTo();

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView from = convertView.findViewById(R.id.favoriteFromStation);
        TextView to = convertView.findViewById(R.id.favoriteToStation);

        from.setText(stationFrom);
        to.setText(stationTo);


        return convertView;
    }
}

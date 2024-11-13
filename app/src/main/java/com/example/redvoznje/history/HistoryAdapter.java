package com.example.redvoznje.history;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.redvoznje.R;

public class HistoryAdapter extends CursorAdapter {

    public HistoryAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.favorite_station_entry, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView favoriteFromStation = view.findViewById(R.id.favoriteFromStation);
        TextView favoriteToStation   = view.findViewById(R.id.favoriteToStation);

        String fromName = cursor.getString(cursor.getColumnIndexOrThrow(HistoryDatabase.ROW_FROM_STATION));
        String toName = cursor.getString(cursor.getColumnIndexOrThrow(HistoryDatabase.ROW_TO_STATION));

        favoriteFromStation.setText(fromName);
        favoriteToStation.setText(toName);
    }


}

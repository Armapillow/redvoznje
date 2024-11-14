package com.example.redvoznje.history;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redvoznje.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final Context context;
    private Cursor cursor;

    public HistoryAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.favorite_station_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.cursor.moveToPosition(position);
        String fromName = cursor.getString(cursor.getColumnIndexOrThrow(HistoryDatabase.ROW_FROM_STATION));
        String toName = cursor.getString(cursor.getColumnIndexOrThrow(HistoryDatabase.ROW_TO_STATION));

        holder.fromStation.setText(fromName);
        holder.toStation.setText(toName);
    }


    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }


    // FIXME: drugaciji pristup nadji
    public void swapCursor(Cursor newCursor) {
        if (this.cursor != null) {
            this.cursor.close(); // Close the old cursor
        }
        this.cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged(); // Notify adapter of data change
        }
    }

    // Za svaki pojedinacni element u recycle view-u
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromStation;
        TextView toStation;

        public ViewHolder(View view) {
            super(view);
            fromStation = view.findViewById(R.id.favoriteFromStation);
            toStation = view.findViewById(R.id.favoriteToStation);
        }

    }


}

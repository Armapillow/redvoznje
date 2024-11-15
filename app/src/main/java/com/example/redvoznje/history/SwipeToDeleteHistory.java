package com.example.redvoznje.history;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteHistory extends ItemTouchHelper.SimpleCallback {

    private final HistoryAdapter historyAdapter;
    private final ColorDrawable background;

    public SwipeToDeleteHistory(HistoryAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.historyAdapter = adapter;
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition(); // Get position in the adapter
        if (position != RecyclerView.NO_POSITION) {
            long itemID = historyAdapter.getItemId(position);
//            SQLiteDatabase db = HistoryDatabase.database();
//            db.delete(HistoryDatabase.TABLE_HISTORY, "_id = ?", new String[]{String.valueOf(itemID)});
            this.historyAdapter.removeFromPosition(itemID);

            // skloni iz baze
//            int pos = viewHolder.getAdapterPosition();
//            if (pos != RecyclerView.NO_POSITION) {
//                this.historyAdapter.removeFromPosition(pos);
//            }
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (isCurrentlyActive) {
            viewHolder.itemView.setBackgroundColor(0xff3e3e); // Change color on swipe
        } else {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT); // Reset color after swipe
        }

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
}

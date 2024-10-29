package com.example.redvoznje.stations;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redvoznje.R;

import java.util.ArrayList;


public class PopulateTimeTableAdapter extends ArrayAdapter<TimeTableEntry> {

    private Context context;
    private int resource;
    private ArrayList<TimeTableEntry> entryList;


    public PopulateTimeTableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TimeTableEntry> entries) {
        super(context, resource, entries);

        this.context = context;
        this.resource = resource;
        this.entryList = entries;
    }


    // TODO: da bi se ubrzalo treba holder klasa ??

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String trainID = getItem(position).getTrainID().trim();
        String departureTime = getItem(position).getDepartureTime().trim();
        String arrivalTime = getItem(position).getArrivalTime().trim();
        String travelTime  = getItem(position).getTravelTime().trim();
        String rangOfTrain = getItem(position).getRangOfTrain().trim();
        String arrivalDate = getItem(position).getArrivalDate().trim();
        String departureDate = getItem(position).getDepartureDate().trim();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView tvTrainType = convertView.findViewById(R.id.textViewTrainType);
        TextView tvTrainNumber = convertView.findViewById(R.id.textViewTrainNumber);
        TextView tvDepartureTime = convertView.findViewById(R.id.textViewDeparture);
        TextView tvArrivalTime = convertView.findViewById(R.id.textViewArrival);
        TextView tvTravelTime = convertView.findViewById(R.id.textViewTravelTime);
        TextView tvDepartureDate = convertView.findViewById(R.id.textViewDepartureDate);
        TextView tvArrivalDate = convertView.findViewById(R.id.textViewArrivalDate);


        if (rangOfTrain.contains("BG")) {
            tvTrainType.setTypeface(null, Typeface.BOLD);
            tvTrainType.setText(rangOfTrain);
        } else {
            tvTrainType.setText(rangOfTrain);
        }
        tvTrainNumber.setText(trainID);
        tvDepartureTime.setText(departureTime);
        tvArrivalTime.setText(arrivalTime);
        tvTravelTime.setText(travelTime);
        tvDepartureDate.setText(departureDate);
        tvArrivalDate.setText(arrivalDate);




        return convertView;
    }

    @Override
    public int getCount() {
        return entryList.size();
    }
}

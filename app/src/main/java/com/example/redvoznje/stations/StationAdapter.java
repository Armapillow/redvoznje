package com.example.redvoznje.stations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redvoznje.R;

import java.util.ArrayList;
import java.util.Comparator;

public class StationAdapter extends ArrayAdapter<Station> {

    Context context;
    ArrayList<Station> allStations;

    public StationAdapter(@NonNull Context context, ArrayList<Station> stations) {
        super(context, 0, stations);

        this.context = context;

        // NOTE: JAKO BITNO!!
        // Ovo ne radi: this.allStations = stations;
        this.allStations = new ArrayList<>(stations);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        if (convertView == null) {
//            LayoutInflater inflater = LayoutInflater.from(this.context);
//            convertView = inflater.inflate(stationDropEntryID, parent, false);
//        }


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.autocomplete_drop_entry, parent, false);
        }

        Station station = getItem(position);
        assert station != null;
        String stationName = station.getName();
        TextView stationDropEntry = convertView.findViewById(R.id.textViewDropStationName);
        stationDropEntry.setText(stationName);


        return convertView;
    }

    private final Filter stationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Station> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(allStations);
            } else {
                String filter = constraint.toString().toLowerCase().trim();
                for (Station station : allStations) {
                    String stationName = station.getName().toLowerCase();

                    // TODO: mora da postoji bolji nacin, da prvo idu one koje pocinju, pa onda ostale
                    //       fuzzy search
                    if (stationName.startsWith(filter)) {
                        suggestions.add(station);
                    } else if (stationName.contains(filter));
//                        suggestions.add(station);
                }
            }

//            suggestions.sort((s1, s2) -> { return s1.getName().compareTo(s2.getName()); });

            results.values = suggestions;
            results.count  = suggestions.size();

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Station s = (Station) resultValue;
            return s.getName();
        }
    };



    @NonNull
    @Override
    public Filter getFilter() {
        return stationFilter;
    }

}

package com.example.redvoznje;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.redvoznje.stations.PopulateTimeTableAdapter;
import com.example.redvoznje.stations.TimeTableEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class TimeTableActivity extends AppCompatActivity {


    private final ArrayList<TimeTableEntry> timeTableEntriesList = new ArrayList<>();

    ListView listViewTimetable;


    private class TABLE {
        public final static int TRAIN_ID = 0;
        public final static int DEPARTURE = 1;
        public final static int DEPARTURE_DATE = 2;
        public final static int ARRIVAL = 3;
        public final static int ARRIVAL_DATE = 4;
        public final static int DELAY = 5;
        public final static int TRAVEL_TIME = 6;
        public final static int RANG = 7;
        public final static int OFFER = 8;
        public final static int NOTE = 9;
        public final static int DETAILS = 10;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_main);

        String timetableTitle = getIntent().getStringExtra("timetableTitle");
        String rawTimetableResponse = getIntent().getStringExtra("timetableResponse");
        String timetableURL = getIntent().getStringExtra(MainActivity.INTENT_EXTRA_URL);

        TextView textViewTitle = findViewById(R.id.textViewTimeTableTitle);

        textViewTitle.setText(timetableTitle);

        ImageButton buttonRefresh = findViewById(R.id.buttonRefreshTimetable);
        buttonRefresh.setOnClickListener(v -> {
            Toast.makeText(this, "Nije implementirano", Toast.LENGTH_SHORT).show();
        });

        listViewTimetable = findViewById(R.id.listViewTimeTable);


        populateTimetable(rawTimetableResponse);

        TextView timeTableURLTextView = findViewById(R.id.timetable_link_textview);
        timeTableURLTextView.setOnClickListener(v -> openBrowser(timetableURL));


    }

    private void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));

        try {
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(TimeTableActivity.this, "nema petrazivaca da otvori link", Toast.LENGTH_SHORT).show();
        }
    }


    private void populateTimetable(String rawResponse) {


        final LoadingDialog loadingDialog = new LoadingDialog("Dohvatanje redava voznje...");
        loadingDialog.setCancelable(false);


        timeTableEntriesList.clear();
        listViewTimetable.setAdapter(null);

        Document doc = Jsoup.parse(rawResponse);

        Element table = doc.select("table").first();
        if (table == null) {
            loadingDialog.setMessage("Trenutno ne postoji voz na trazenoj relaciji");
            loadingDialog.show(getSupportFragmentManager(), "123");
        } else {
            Elements rows = table.select("tr");
            for (int i = 1; i < rows.size(); i++) { // first row: objasnjenja
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() > 1) {  // preskacemo class="tbig", to je za dugme

                    StringBuilder sb = new StringBuilder();


                    String trainID = cols.get(TABLE.TRAIN_ID).text();
                    String departureTime = cols.get(TABLE.DEPARTURE).text();
                    String arrivalTime = cols.get(TABLE.ARRIVAL).text();
//                                    sb.append(cols.get(TABLE.DELAY).text());
//                                    sb.append("\n");
                    String travelTime = cols.get(TABLE.TRAVEL_TIME).text();
                    String trainType = cols.get(TABLE.RANG).select("img").attr("title");
                    String departureDate = cols.get(TABLE.DEPARTURE_DATE).text();
                    String arrivalDate   = cols.get(TABLE.ARRIVAL_DATE).text();

                    sb.append(cols.get(TABLE.DETAILS).select("button").attr("data-idvoza"));


                    TimeTableEntry t = new TimeTableEntry(trainID, departureTime, arrivalTime, travelTime, trainType);
                    t.setArrivalDate(arrivalDate);
                    t.setDepartureDate(departureDate);
                    timeTableEntriesList.add(t);

//                                    timeTableEntriesList.add(new TimeTableEntry(trainID, departureTime, arrivalTime, travelTime, trainType));

//                                    mainView.setText(sb.toString());

                }
            }

        }


        if (timeTableEntriesList.size() != 0) {
            PopulateTimeTableAdapter adapter = new PopulateTimeTableAdapter(TimeTableActivity.this, R.layout.timetable_entry, timeTableEntriesList);
            listViewTimetable.setAdapter(adapter);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

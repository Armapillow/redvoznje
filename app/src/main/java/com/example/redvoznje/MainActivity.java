package com.example.redvoznje;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redvoznje.helpers.CustomTime;
import com.example.redvoznje.helpers.Formats;
import com.example.redvoznje.history.HistoryAdapter;
import com.example.redvoznje.history.HistoryEntry;
import com.example.redvoznje.news.NewsActivity;
import com.example.redvoznje.stations.Station;
import com.example.redvoznje.stations.StationAdapter;
import com.example.redvoznje.stations.StationFillAll;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Queue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private static final String NEWS_URL = "https://www.srbvoz.rs/wp-json/wp/v2/info_post";
    private static final String URL_TIMETABLE_BASE = "https://w3.srbvoz.rs/redvoznje/direktni/";


    public static final String INTENT_EXTRA_URL = "timetable_url";




    private static final int REQUEST_CODE_NEWS_ACTIVITY = 1;
    private static String cachedJSONString = null;


    private ArrayList<Station> stationList;
    private AutoCompleteTextView textViewFrom;
    private AutoCompleteTextView textViewTo;
    private String fromStationID;
    private String toStationID;
    private String fromStation;
    private String toStation;
    private TextView tvSearch;

    private TextView buttonDepartureTime;

    String timeTableTitle = "";


    // TODO: temporarily
    String previewText;
    private TextView helperTextView;
    private TextView timePicker;
    CustomTime time;
    ArrayList<HistoryEntry> history = new ArrayList<>();
    ListView historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions_form);


        stationList = StationFillAll.fillAllStation();


        tvSearch = findViewById(R.id.btnSearch);
        tvSearch.setOnClickListener(v -> handleSearch());

        Button btnNews = findViewById(R.id.newsButton);
        btnNews.setOnClickListener(v -> {
            if (cachedJSONString != null) {
                Log.d("MOJA", "Velicina u bajtovima: " + cachedJSONString.length());
                startNewsActivityWithJSON(cachedJSONString);
            } else {
                downloadNewsJSON();
            }
        });



        textViewFrom = findViewById(R.id.textViewFrom);
        textViewTo = findViewById(R.id.textViewTo);
        timePicker = findViewById(R.id.buttonTimePicker);

        helperTextView = findViewById(R.id.textViewHelper);

        historyListView = findViewById(R.id.historyList);


        ImageView swapImgView = findViewById(R.id.swapButton);
        swapImgView.setOnClickListener(v -> {
            String tmp = fromStation;
            fromStation = toStation;
            toStation = tmp;

            tmp = fromStationID;
            fromStationID = toStationID;
            toStationID = tmp;

            textViewFrom.setText(fromStation);
            textViewTo.setText(toStation);
        });

        StationAdapter stationAdapter = new StationAdapter(MainActivity.this, stationList);

        textViewFrom.setAdapter(stationAdapter);
        textViewTo.setAdapter(stationAdapter);

        textViewFrom.setOnItemClickListener((parent, view, position, id) -> {
            fromStationID = stationAdapter.getItem(position).getId();
            textViewTo.requestFocus();
        });
        textViewTo.setOnItemClickListener((parent, view, position, id) -> {
            toStationID = stationAdapter.getItem(position).getId();
        });

        ImageButton clearFromStation = findViewById(R.id.clearFromTextView);
        ImageButton clearToStation = findViewById(R.id.clearToTextView);



        // TODO: da li moze da se apstrahuje??
        //       input manager
        clearToStation.setOnClickListener(v -> {
            textViewTo.setText("");
            textViewTo.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
            if (null != inputManager)
                inputManager.showSoftInput(textViewTo, InputMethodManager.SHOW_IMPLICIT);

        });

        clearFromStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewFrom.setText("");
                textViewFrom.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                if (null != inputManager)
                    inputManager.showSoftInput(textViewFrom, InputMethodManager.SHOW_IMPLICIT);
//                if (v instanceof AutoCompleteTextView) {
//                    AutoCompleteTextView actv = (AutoCompleteTextView) v;
//                    actv.setText("");
//                    actv.requestFocus();
//                }
            }
        });


        buttonDepartureTime = findViewById(R.id.buttonDepartureTime);
        buttonDepartureTime.setOnClickListener(relativeTimeListener);
    }


    private final OnClickListener relativeTimeListener = v -> {

        final int[] relativeTimeValues = getResources().getIntArray(R.array.set_time_relative);
        final String[] relativeTimeStrings = new String[relativeTimeValues.length + 1];
        relativeTimeStrings[relativeTimeValues.length] = "izaberi vreme"; // fixed time

        for (int i = 0; i < relativeTimeValues.length; i++) {
            if (relativeTimeValues[i] == 0)
                relativeTimeStrings[i] = "sada";
            else if (relativeTimeValues[i] == 30)
                relativeTimeStrings[i] = "za 30 min";
            else if (relativeTimeValues[i] == 60)
                relativeTimeStrings[i] = "za 1 sat";
            else if (relativeTimeValues[i] == 120)
                relativeTimeStrings[i] = "za 2 sata";
        }

        final DialogBuilder builder = DialogBuilder.get(MainActivity.this);
        builder.setTitle("Polazak:");
        builder.setItems(relativeTimeStrings, (dialog, which) -> {
            if (which < relativeTimeValues.length) {
                final int minutes = relativeTimeValues[which];
                time = new CustomTime.Relative(minutes * DateUtils.MINUTE_IN_MILLIS);
            } else {
                time = new CustomTime.Fixed(System.currentTimeMillis());
            }
            updateTimeGUI();
        });

        builder.show();
    };

    private void updateTimeGUI() {

        if (time == null) {
            timePicker.setVisibility(View.GONE);
        } else if (time instanceof CustomTime.Fixed) {

            buttonDepartureTime.setOnClickListener(customDateListener);
            buttonDepartureTime.setText(Formats.formatDate(this, time.getTimeInMs()));

            timePicker.setVisibility(View.VISIBLE);
            timePicker.setText(Formats.formatTime(this, time.getTimeInMs()));
            timePicker.setOnClickListener(customTimeListener);

        } else if (time instanceof CustomTime.Relative) {
            buttonDepartureTime.setText(Formats.formatRelativeDeparture(time.getTimeInMs()));

            timePicker.setVisibility(View.GONE);
        }


    }

    private final OnClickListener customTimeListener = v -> {
        final Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time.getTimeInMs()); // ako izaberemo datum koji nije trenutni
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(MainActivity.this, 0, (view, hour1, minute1) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour1);
            calendar.set(Calendar.MINUTE, minute1);

            time = new CustomTime.Fixed(calendar.getTimeInMillis());
            updateTimeGUI();

        }, hour, minute, DateFormat.is24HourFormat(MainActivity.this)).show();
    };

    private final OnClickListener customDateListener = v -> {
        final Calendar calendar = new GregorianCalendar();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(MainActivity.this, 0, (view, year1, month1, day1) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, day1);

            time = new CustomTime.Fixed(calendar.getTimeInMillis());
            updateTimeGUI();

        }, year, month, day).show();
    };

    private void handleSearch() {

        textViewFrom.clearFocus();
        textViewTo.clearFocus();

//        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
//        if (imm != null)
//            imm.hideSoftInputFromWindow(tvSearch.getWindowToken(), 0);

        fromStation = textViewFrom.getText().toString().trim();
        toStation = textViewTo.getText().toString().trim();

        if (fromStation.equalsIgnoreCase("")) {
            Toast.makeText(MainActivity.this, "Izaberi početnu stanicu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (toStation.equalsIgnoreCase("")) {
            Toast.makeText(MainActivity.this, "Izaberi krajnju stanicu", Toast.LENGTH_SHORT).show();
            return;
        }

        previewText = fromStation + " (" + fromStationID + ")" + " --> " + toStation + " (" + toStationID + ")";
        timeTableTitle = fromStation + "  –  " + toStation;


        // Popuni istoriju
        HistoryEntry entry = new HistoryEntry(fromStation, fromStationID, toStation, toStationID);
        history.add(entry);
        HistoryAdapter historyAdapter = new HistoryAdapter(MainActivity.this, R.layout.favorite_station_entry, history);
        historyListView.setAdapter(historyAdapter);



        String url = buildUrl(fromStation, toStation);
        handleURL(url);
    }

    private void handleURL(String url) {

        final LoadingDialog onErrorDilaog = new LoadingDialog();
        final String whatIsThis = "123";



        // TODO: deprecated - https://m1.material.io/components/progress-activity.html
        //                    https://developer.android.com/reference/android/widget/ProgressBar
        //                    https://developer.android.com/develop/ui/views/components/dialogs
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Dohvatanje reda voznje");
        progressDialog.setCanceledOnTouchOutside(false);


        Handler handler = new Handler(Looper.getMainLooper());
        Call call = new OkHttpClient().newCall(new Request.Builder().url(url).build());

        progressDialog.show();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                progressDialog.dismiss();

                onErrorDilaog.setMessage("Greska pri dohvatanju putanje..\nProveriti konekciju!");
                onErrorDilaog.show(getSupportFragmentManager(), whatIsThis);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                progressDialog.dismiss();

                String rawResponse = response.body().string();

                handler.post( () -> {

                    Intent timetableIntent = new Intent(MainActivity.this, TimeTableActivity.class);
                    timetableIntent.putExtra("timetableTitle", timeTableTitle);
                    timetableIntent.putExtra("timetableResponse", rawResponse);
                    timetableIntent.putExtra(INTENT_EXTRA_URL, url);
                    startActivity(timetableIntent);
                });
            }
        });

    }

    private String buildUrl(String fromStation, String toStation) {

        StringBuilder baseURL = new StringBuilder(URL_TIMETABLE_BASE);
        baseURL.append(fromStation).append("/").append(fromStationID).append("/");
        baseURL.append(toStation).append("/").append(toStationID).append("/");

        String date = Formats.getFormatForUrl(time);

        Log.d("DATUM", date);
        previewText += "\n\n" + date + "\n\n history size: " + history.size();
        helperTextView.setText(previewText);

        baseURL.append(date).append("/sr");
        String urlString = baseURL.toString().replace(" ", "%20");

        Log.d("DATUM", baseURL.toString());
        Log.d("DATUM", urlString);


        return urlString;
    }

    private void downloadNewsJSON() {

        LoadingDialog onErrorDialog = new LoadingDialog();
        final String whatIsThis = "123";

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Dohvatanje vesti");
        progressDialog.setCanceledOnTouchOutside(false);

        Handler handler = new Handler(Looper.getMainLooper());
        Call call = new OkHttpClient().newCall(new Request.Builder().url(NEWS_URL).build());

        progressDialog.show();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // handle this
                progressDialog.dismiss();

                onErrorDialog.setMessage("Greska prilikom dohvatanja vesti..\nProveri konekciju!");
                onErrorDialog.show(getSupportFragmentManager(), whatIsThis);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                // FIXME: stavi if
                assert response.body() != null;
                String rawResponse = response.body().string();

                handler.post(() -> {
                    progressDialog.dismiss();

                    cachedJSONString = rawResponse.trim();
                    startNewsActivityWithJSON(cachedJSONString);

                });
            }
        });
    }


    private void startNewsActivityWithJSON(String rawNewsJSON) {
        Intent newsIntent = new Intent(MainActivity.this, NewsActivity.class);
        newsIntent.putExtra("jsonString", rawNewsJSON);
        // FIXME: bolji nacin??
        startActivityForResult(newsIntent, REQUEST_CODE_NEWS_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NEWS_ACTIVITY && resultCode == RESULT_OK && data != null) {
            cachedJSONString = data.getStringExtra("modifiedJSON");
        }
    }

    private final OnClickListener departureListener = v -> {
//      final AlertDialog builder = new AlertDialog();
    };
}
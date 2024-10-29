package com.example.redvoznje.news;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.redvoznje.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NewsActivity extends AppCompatActivity {
    private static final String NEWS_URL = "https://www.srbvoz.rs/wp-json/wp/v2/info_post";
    private static final String NEWS_LINK = "https://srbijavoz.rs/informacije/";
    private ListView mNewsListView;

    private Handler backgroundHandler;
    private String rawNewsJSON;


    private ArrayList<News> mNewsList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);

        rawNewsJSON = getIntent().getStringExtra("jsonString");

        mNewsListView = findViewById(R.id.listViewNewsList);
        ImageButton mRefreshButton = findViewById(R.id.buttonRefreshNews);

        mRefreshButton.setOnClickListener(v -> getNewsFromInternet());

        displayNews(rawNewsJSON);

        TextView newsURLTextView = findViewById(R.id.textViewNewsURL);
        newsURLTextView.setOnClickListener( v -> openBrowser() );


    }

    private void openBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(NEWS_LINK));
        // TODO: mozda je bolje sa if-om: browserIntent.resolveActivity(getPackageManager()) != null
        try {
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Nema pretraživača da otvori link", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayNews(String rawJsonData) {

        mNewsList.clear();
        mNewsList = ParseNews.getNewsList(rawJsonData);


//        NewsAdapter adapter = new NewsAdapter(NewsActivity.this, R.layout.news_card_layout, mNewsList);
        ExpandableNewsAdapter adapter = new ExpandableNewsAdapter(NewsActivity.this, R.layout.expandable_news_entry, mNewsList);
        mNewsListView.setAdapter(adapter);

    }

    private void getNewsFromInternet() {


        final ProgressDialog progressDialog = new ProgressDialog(NewsActivity.this);
        progressDialog.setMessage("Dohvatanje vesti...");
        progressDialog.setCanceledOnTouchOutside(false);

        backgroundHandler = new Handler(Looper.getMainLooper());
        Call call = new OkHttpClient().newCall(new Request.Builder().url(NEWS_URL).build());


        progressDialog.show();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                progressDialog.dismiss();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // TODO: da li postoji bolje resenje?
                assert response.body() != null;
                String rawJson = response.body().string();

                backgroundHandler.post(() -> {
                    progressDialog.dismiss();
                    rawNewsJSON = rawJson.trim();

                    // TODO: mora li ovde?? msm da mora!
                    displayNews(rawNewsJSON);
                });
            }
        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

//        mNewsListView.setAdapter(null);
//        Toast.makeText(this, "ON DESTROY", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // TODO: da li postoji bolji nacin, ChatGPT generisao
        //       how to pass data between activities:
        //       If i have json string that is passed from mainactivity
        //       to the newsactivity how to update it from newsActivity,
        //       so that when i go back to the mainActivity it will be updated
        Intent intent = new Intent();
        intent.putExtra("modifiedJSON", rawNewsJSON);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void showME(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();

    }
}

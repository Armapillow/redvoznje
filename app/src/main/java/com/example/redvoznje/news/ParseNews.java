package com.example.redvoznje.news;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseNews {
    public static String RENDERED = "rendered";
    public static String DATE = "date";
    public static String CONTENT = "content";
    public static String TITLE = "title";



    public static ArrayList<News> getNewsList(String rawData) {
        final ArrayList<News> newsList = new ArrayList<>();

        try {
            JSONArray ja = new JSONArray(rawData);

            for (int i = 0; i < ja.length(); i++) {
                JSONObject post = new JSONObject(ja.get(i).toString());

                // Naslov
                JSONObject joTitle = new JSONObject(post.get(TITLE).toString());
                String title = joTitle.get(RENDERED).toString();
                String date = post.get(DATE).toString();

                // Sadrzaj
                JSONObject jc = new JSONObject(post.get(CONTENT).toString());
                String content = jc.get(RENDERED).toString();

                newsList.add(
                        new News(title, date, content)
                );
            }

        } catch (JSONException e) {
            return null;
        }

        return newsList;
    }

}

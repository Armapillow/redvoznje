package com.example.redvoznje.news;

import android.os.Build;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class News {

    private String title;
    private String date;
    private String content;

    public News(String rawTitle, String rawDate, String rawContent) {
        this.title = trimHTML(rawTitle.trim());
        this.date  = parseDate(rawDate);
        this.content = trimHTML(rawContent);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private static String parseDate(String dateString) {
        DateTimeFormatter inputFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        }
        LocalDateTime dateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(dateString, inputFormatter);
        }
        DateTimeFormatter outputFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime.format(outputFormatter);
        }

        return dateString;
    }


    private static String trimHTML(String string) {

        String withoutHTML = string.replaceAll("<p.*?>", "").replaceAll("</p>", "\n");
        withoutHTML = withoutHTML.replaceAll("<span.*?>", "").replace("</span>", "").trim();
        withoutHTML = withoutHTML.replaceAll("<div.*?>", "").replaceAll("</div>", "").trim();
        withoutHTML = withoutHTML.replaceAll("<h3.*?>", "").replaceAll("</h3>", "").trim();
        withoutHTML = withoutHTML.replaceAll("<br.*?/>", "").trim();
        withoutHTML = withoutHTML.replaceAll("&#8211;", "–").trim();
        return withoutHTML;
    }

    @NonNull
    @Override
    public String toString() {
        return title + " -- " + date + "\n\n" + content;
    }

}

package com.example.redvoznje.helpers;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.example.redvoznje.helpers.CustomTime;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class Formats {

    public static String formatDate(final Context context, final long time) {

        if (DateUtils.isToday(time))
            return "danas";


        return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_SHOW_WEEKDAY
                | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_ABBREV_MONTH);
    }

    public static String formatTime(final Context context, final long time) {
        return DateFormat.getTimeFormat(context).format(time);
    }


    public static String formatRelativeDeparture(final long diffMs) {
        final long minutes = Math.round((float) diffMs / DateUtils.MINUTE_IN_MILLIS);
        if (minutes == 120)
            return "za 2 sata";
        else if (minutes == 60)
            return "za 1 sat";
        else if (minutes == 30)
            return "za 30 min";
        else
            return "sada";
    }

    public static String getFormatForUrl(CustomTime time) {

        // TODO: da li dobro??
        // FIXME: kako bez if-a

        LocalDateTime currentDateTime = null;
        Duration duration = null;
        DateTimeFormatter formatter = null;
        String result = "";
        String format = "dd.MM.yyyy/HHmm";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (time instanceof CustomTime.Fixed) {
                Date date = new Date(time.getTimeInMs());
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                result = sdf.format(date);

            } else if (time instanceof CustomTime.Relative) {
                currentDateTime = LocalDateTime.now();
                duration = Duration.ofMillis(((CustomTime.Relative) time).timeMs);
                currentDateTime = currentDateTime.plus(duration);
                formatter = DateTimeFormatter.ofPattern(format);
                result = currentDateTime.format(formatter);
            } else {
                currentDateTime = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern(format);
                result = currentDateTime.format(formatter);

            }


        }

        return result;
    }
}

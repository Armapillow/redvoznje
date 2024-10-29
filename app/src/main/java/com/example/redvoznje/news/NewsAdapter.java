package com.example.redvoznje.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redvoznje.R;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    Context mContext;
    private int mResources;
    private ArrayList<News> mNewsList;

    private static class ViewHolder {
        TextView title;
        TextView date;
        TextView content;
    }

    public NewsAdapter(@NonNull Context context, int resource, ArrayList<News> newsList) {
        super(context, resource, newsList);

        this.mContext = context;
        this.mResources = resource;
        this.mNewsList = newsList;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String title = getItem(position).getTitle();
        String date = getItem(position).getDate();
        String content = getItem(position).getContent();


        final View result;
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResources, parent, false);

            holder = new ViewHolder();

            holder.title = convertView.findViewById(R.id.expandableTitle);
            holder.date  = convertView.findViewById(R.id.expandableDate);
            holder.content  = convertView.findViewById(R.id.expandableContent);


            result  = convertView;
            convertView.setTag(holder);


        } else {

            holder = (ViewHolder) convertView.getTag();
            result = convertView;

        }

        holder.title.setText(title);
        holder.date.setText(date);
        holder.content.setText(content);


        return convertView;
    }
}

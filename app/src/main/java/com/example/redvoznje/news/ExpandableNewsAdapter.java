package com.example.redvoznje.news;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redvoznje.R;

import java.util.ArrayList;

public class ExpandableNewsAdapter extends ArrayAdapter<News> {

    private Context mContext;
    private int mResource;
    private ArrayList<News> mNewsList;

    public ExpandableNewsAdapter(@NonNull Context context, int resource, ArrayList<News> newsList) {
        super(context, resource, newsList);

        this.mContext = context;
        this.mResource = resource;
        this.mNewsList = newsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String title = getItem(position).getTitle();
        String date = getItem(position).getDate();
        String content = getItem(position).getContent();


       if (convertView == null) {
           LayoutInflater inflater = LayoutInflater.from(mContext);
           convertView = inflater.inflate(mResource, parent, false);
       }

       TextView textViewTitle = convertView.findViewById(R.id.expandableTitle);
       TextView textViewDate = convertView.findViewById(R.id.expandableDate);
       TextView textViewContent = convertView.findViewById(R.id.expandableContent);

       textViewTitle.setText(title);
       textViewDate.setText(date);
       textViewContent.setText(content);

        LinearLayout expandableLL = convertView.findViewById(R.id.exanableLL);

        expandableLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewContent.getVisibility() == View.GONE) {
                    textViewContent.setVisibility(View.VISIBLE);
                } else {textViewContent.setVisibility(View.GONE);
                }
            }
        });



        return convertView;
    }
}

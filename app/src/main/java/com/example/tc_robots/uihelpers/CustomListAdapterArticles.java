package com.example.tc_robots.uihelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.Article;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapterArticles extends ArrayAdapter<Article> {
    public CustomListAdapterArticles(@NonNull Context context, List<Article> articleList) {
        super(context, R.layout.list_view_element,articleList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article article = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_element,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.icon);
        TextView firstLine = convertView.findViewById(R.id.firstLine);
        TextView secondLine = convertView.findViewById(R.id.secondLine);

        imageView.setImageResource(article.getImageID());
        firstLine.setText(article.getName());
        secondLine.setText("Available Quantity: "+article.getAvailableQuantityInStorage());

        return convertView;
    }
}

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
import com.example.tc_robots.backend.Alert;
import com.example.tc_robots.backend.Article;

import java.util.List;

public class CustomListAdapterAlerts extends ArrayAdapter<Alert> {

    public CustomListAdapterAlerts(@NonNull Context context, List<Alert> alertList) {
        super(context, R.layout.list_view_element_alert,alertList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Alert alert = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_element_alert,parent,false);
        }

        TextView errorTitle = convertView.findViewById(R.id.errorTitle);
        TextView errorText = convertView.findViewById(R.id.errorText);
        TextView date = convertView.findViewById(R.id.date);

        errorTitle.setText(alert.getErrorCode());
        errorText.setText(alert.getErrorText());
        date.setText(String.valueOf(alert.getDate()));

        return convertView;
    }
}

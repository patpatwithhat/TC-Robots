package com.example.tc_robots.uihelpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.Alert;
import com.example.tc_robots.backend.Article;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CustomListAdapterAlerts extends ArrayAdapter<Alert> {
    List<Alert> alertList;
    private static final String TAG = "CustomListAdapterAlerts";

    public CustomListAdapterAlerts(@NonNull Context context, List<Alert> alertList) {
        super(context, R.layout.list_view_element_alert, alertList);
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Alert alert = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_element_alert, parent, false);
        }

        TextView errorTitle = convertView.findViewById(R.id.errorTitle);
        TextView errorText = convertView.findViewById(R.id.errorText);
        TextView date = convertView.findViewById(R.id.date);
        MaterialButton button = convertView.findViewById(R.id.btn_close_alert);

        errorTitle.setText(alert.getErrorCode());
        errorText.setText(alert.getErrorText());
        date.setText(alert.getDate().getCorrectTimeInfo());

        button.setTag(position);
        button.setOnClickListener(this::onClick);

        return convertView;
    }

    public void onClick(View view) {
        remove((int) view.getTag());
    }

    public void remove(int position) {
        this.alertList.remove(position);
        notifyDataSetChanged();
    }
}

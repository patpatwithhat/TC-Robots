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
import androidx.core.content.ContextCompat;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.monitoring.Alert;
import com.example.tc_robots.backend.monitoring.ErrorType;
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
        ImageView imageView = convertView.findViewById(R.id.icon);
        MaterialButton button = convertView.findViewById(R.id.btn_close_alert);

        errorTitle.setText(alert.getErrorCode());
        errorText.setText(alert.getErrorText());
        date.setText(alert.getDate().getCorrectTimeInfo());
        adaptIconToErrorType(imageView, alert.getErrorType());

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

    public void adaptIconToErrorType(ImageView imageView, ErrorType errorType) {
        switch (errorType) {
            case INFO:
                imageView.getDrawable().setTint(ContextCompat.getColor(this.getContext(), R.color.info_grey));
                break;
            case ERROR:
                imageView.setImageResource(R.drawable.ic_error);
                imageView.getDrawable().setTint(ContextCompat.getColor(this.getContext(), R.color.error_red));
                break;
            case WARNING:
                imageView.setRotation(180.0f);
                imageView.getDrawable().setTint(ContextCompat.getColor(this.getContext(), R.color.warning_orange));
                break;
            default:
                break;
        }
    }

}

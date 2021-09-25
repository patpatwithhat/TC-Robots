package com.example.tc_robots.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.Alert;
import com.example.tc_robots.backend.Article;
import com.example.tc_robots.backend.CustomDate;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonitoringScreenViewModel extends ViewModel {

    private final MutableLiveData<List<Alert>> alertList = new MutableLiveData<>();

    public MonitoringScreenViewModel() {
        uiTest();
    }

    private void uiTest() {
        CustomDate date = new CustomDate();
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusDays(1);
        date.setDate(dateTime);
        Alert alert1 = new Alert("Error 1001", "Was teures brennt! Lass mal gucken gehen, wo das is, ne! Könnt sau gefährlich sein!", date);
        Alert alert2 = new Alert("Error 2013", "Öl fehlt!", new CustomDate());
        List<Alert> alerts = new ArrayList<>();
        alerts.add(alert1);
        alerts.add(alert2);
        alertList.setValue(alerts);
    }

    public LiveData<List<Alert>> getAlertList() {
        return alertList;
    }


}

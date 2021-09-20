package com.example.tc_robots.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.Alert;
import com.example.tc_robots.backend.Article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonitoringScreenViewModel extends ViewModel {

    private final MutableLiveData<List<Alert>> alertList = new MutableLiveData<>();

    public MonitoringScreenViewModel() {
        Alert alert1 = new Alert("Error 1001", "Was teures brennt!", new Date().getTime());
        Alert alert2 = new Alert("Error 2013", "Öl fehlt!", new Date().getTime());
        List<Alert> alerts = new ArrayList<>();
        alerts.add(alert1);
        alerts.add(alert2);
        alertList.setValue(alerts);
    }

    public LiveData<List<Alert>> getAlertList() {
        return alertList;
    }


}

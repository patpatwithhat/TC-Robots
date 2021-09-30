package com.example.tc_robots.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.Alert;
import com.example.tc_robots.backend.Article;
import com.example.tc_robots.backend.CustomDate;
import com.example.tc_robots.backend.ErrorType;
import com.example.tc_robots.uihelpers.ListViewFilter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MonitoringScreenViewModel extends ViewModel {
    private final MutableLiveData<List<Alert>> alertList = new MutableLiveData<>();
    //used to update btn_show_all if filter is active or not
    private final MutableLiveData<Boolean> isFilterActive = new MutableLiveData<>();
    private final ListViewFilter listViewFilter = new ListViewFilter();

    public MonitoringScreenViewModel() {
        uiTest();
    }

    private void uiTest() {
        CustomDate date = new CustomDate();
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusDays(1);
        date.setDate(dateTime);
        Alert alert1 = new Alert(ErrorType.WARNING, "Warning 1001", "Was teures brennt! Lass mal gucken gehen, wo das is, ne! Könnt sau gefährlich sein!", date);
        Alert alert2 = new Alert(ErrorType.ERROR, "Error 2013", "Öl fehlt!", new CustomDate());
        Alert alert3 = new Alert(ErrorType.INFO, "Info 3333", "Neuer Roboter ist da!", date);
        Alert alert4 = new Alert(ErrorType.ERROR, "Error 2345", "Core Temp over 9000", new CustomDate());
        List<Alert> alerts = new ArrayList<>();
        alerts.add(alert4);
        alerts.add(alert1);
        alerts.add(alert2);
        alerts.add(alert3);
        alertList.setValue(alerts);
    }

    public List<Alert> filterForErrorTypeAndSetActiveErrorType(ErrorType errorType) {
        setIsFilterActive(errorType);
        return listViewFilter.filterListByErrorType(Objects.requireNonNull(alertList.getValue()), errorType);
    }

    public List<Alert> getAllAlertsAndClearFilter() {
        setIsFilterActive();
        return getAlertList().getValue();
    }

    public LiveData<List<Alert>> getAlertList() {
        return alertList;
    }

    //filter active, if errorType.length == 0 <= means errorType is Empty
    private void setIsFilterActive(ErrorType... errorType) {
        if (errorType.length == 0) {
            isFilterActive.setValue(false);
        } else {
            isFilterActive.setValue(true);
        }
    }

    public LiveData<Boolean> getIsFilterActive() {
        return isFilterActive;
    }


}

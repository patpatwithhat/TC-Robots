package com.example.tc_robots.ui.monitoringscreen;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.monitoring.Alert;
import com.example.tc_robots.backend.monitoring.CustomDate;
import com.example.tc_robots.backend.monitoring.ErrorType;
import com.example.tc_robots.backend.network.TCPClient;
import com.example.tc_robots.backend.network.TCPClientSet;
import com.example.tc_robots.uihelpers.ListViewFilter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MonitoringScreenViewModel extends ViewModel implements TCPClient.OnMessageReceived {
    private static final String TAG = "MonitoringScreenViewModel";
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
        TCPClient.getInstance().addOnMessageReceivedListener(this);
        TCPClientSet.getInstance().getTcpClientList().forEach(client -> client.addOnMessageReceivedListener(this));
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

    public void removeAlert(int position) {
        List<Alert> alerts = alertList.getValue();
        Objects.requireNonNull(alerts).remove(position);
        alertList.setValue(alerts);
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


    @Override
    public void messageReceived(String message) {
        Log.d(TAG,"from viewmodel: "+message);

        Alert alert = new Alert(message);
    }
}

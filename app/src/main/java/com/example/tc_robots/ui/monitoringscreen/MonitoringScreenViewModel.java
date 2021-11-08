package com.example.tc_robots.ui.monitoringscreen;

import static com.example.tc_robots.Constants.ROBOT_LIST_KEY;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.monitoring.Alert;
import com.example.tc_robots.backend.monitoring.CustomDate;
import com.example.tc_robots.backend.monitoring.ErrorType;
import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.backend.network.TCPClient;
import com.example.tc_robots.backend.network.TCPClientSet;
import com.example.tc_robots.backend.network.TCPMessage;
import com.example.tc_robots.backend.tinyDB.TinyDB;
import com.example.tc_robots.backend.tinyDB.TinySingleton;
import com.example.tc_robots.uihelpers.ListViewFilter;
import com.google.android.material.textview.MaterialTextView;

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
    private Application application;

    public MonitoringScreenViewModel(Application application) {
        uiTest();
        this.application = application;
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
        // TCPClient.getInstance().addOnMessageReceivedListener(this);

        initBackend();
    }


    //TODO: Not called, when activityAddRobot closed
    private void initBackend() {
        List<Robot> robotList = TinySingleton.getInstance().getSavedRobots();
        robotList.forEach(robot -> TCPClientSet.getInstance().createClient(robot));
        robotList.forEach(robot -> {
            robot.getTCPClient().run();
            robot.getTCPClient().addOnMessageReceivedListener(this::messageReceived);
        });
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

/*    private void sendToast(String message){
        Activity activity = this;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                MaterialTextView textView= findViewById(R.id.tv_status);
                textView.setText(message);
            }
        });
    }*/

    @Override
    public void messageReceived(TCPClient client, String message) {
        Log.d(TAG, "new message: " + message);
        try {
            if (Integer.parseInt(message) == R.string.ERROR_TCP_CLIENT) {
                Log.d(TAG, "Trying to reconnect... ");
                client.stopClient();
                Thread.sleep(500);
                client.run();
            }
        } catch (Exception exception) {
            try {
                TCPMessage tcpMessage = new TCPMessage(message);
                client.sendMessage(tcpMessage.getErrorCode() + "Received");
            } catch (Exception e) {
                client.sendMessage("Received Msg, but wrong format");
            }
        }
    }
}

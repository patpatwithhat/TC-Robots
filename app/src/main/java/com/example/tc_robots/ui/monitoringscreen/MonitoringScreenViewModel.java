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
    private final MutableLiveData<List<Alert>> alertList = new MutableLiveData<>(new ArrayList<>());
    //used to update btn_show_all if filter is active or not
    private final MutableLiveData<Boolean> isFilterActive = new MutableLiveData<>();
    private final ListViewFilter listViewFilter = new ListViewFilter();
    private final MutableLiveData<String> lastMsg = new MutableLiveData<>();
    private final MutableLiveData<TCPMessage> lastTCPMsg = new MutableLiveData<>();

    private Application application;

    public MonitoringScreenViewModel(Application application) {
        //  uiTest();
        initBackend();
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

    public void newIncomingTCPMsg(TCPMessage tcpMessage) {
        Alert newAlert = new Alert(ErrorType.ERROR, tcpMessage.getErrorCode(), tcpMessage.getConfirmationText() + tcpMessage.getMetaInfo());
        addAlert(newAlert);
    }

    public List<Alert> filterForErrorTypeAndSetActiveErrorType(ErrorType errorType) {
        setIsFilterActive(errorType);
        return listViewFilter.filterListByErrorType(Objects.requireNonNull(alertList.getValue()), errorType);
    }

    public List<Alert> getAllAlertsAndClearFilter() {
        setIsFilterActive();
        return getAlertList().getValue();
    }

    public void addAlert(Alert alert) {
        List<Alert> list = getAlertList().getValue();
        Objects.requireNonNull(list).add(alert);
        alertList.setValue(list);
    }

    public LiveData<String> getLastMsgString() {
        return lastMsg;
    }

    public LiveData<TCPMessage> getLastTCPMsg() {
        return lastTCPMsg;
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

    //TODO: Pass Robot with Message or Hold Message List in Robot
    @Override
    public void messageReceived(Robot robot, String message) {
        Log.d(TAG, "new message: " + message);

        try {
            if (message.equals("Shutdown acknowledged") || Integer.parseInt(message) == R.string.ERROR_TCP_CLIENT) {
                Log.d(TAG, "Trying to reconnect... ");
                lastMsg.postValue("ERROR_TCP_CLIENT");
                robot.getTCPClient().stopClient();
                Thread.sleep(5000);
                robot.getTCPClient().run();
            }
        } catch (Exception exception) {
            try {
                lastMsg.postValue(message);
                TCPMessage tcpMessage = new TCPMessage(message);
                lastTCPMsg.postValue(tcpMessage);
                robot.getTCPClient().sendMessage(tcpMessage.getErrorCode() + "Received");
            } catch (Exception e) {
                robot.getTCPClient().sendMessage("Received Msg, but wrong format");
            }
        }
    }
}

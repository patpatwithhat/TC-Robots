package com.example.tc_robots.ui.monitoringscreen;

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
import com.example.tc_robots.uihelpers.ListViewFilter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        //uiTest();
        this.application = application;
        initOnMessageReceivedListeners();
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


    private void initOnMessageReceivedListeners() {
        TCPClientSet.getInstance().getTcpClientList().forEach(client -> client.addOnMessageReceivedListener(this));
    }

    public void newIncomingTCPMsg(TCPMessage tcpMessage) {
        if (tcpMessage.getMetaInfo() == 0) {
            Alert newAlert = new Alert(ErrorType.WARNING, tcpMessage.getErrorCode(), tcpMessage.getConfirmationText());
            addAlert(newAlert);
            return;
        }
        float val = (float) tcpMessage.getMetaInfo();
        String metainfo = String.valueOf(val/100);
        Alert newAlert = new Alert(ErrorType.WARNING, tcpMessage.getErrorCode(), tcpMessage.getConfirmationText() +metainfo);
        if (tcpMessage.getConfirmationText().equals("temperatur")) {
            newAlert.setErrorText("Temperatur im kritischen Bereich! " + metainfo + "°C");
            newAlert.setErrorType(ErrorType.ERROR);
        }
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

    public LiveData<List<Alert>> getAlertList() {
        return alertList;
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
    public void messageReceived(Robot robot, String message) {
        Log.d(TAG, "new message: " + message);

        try {
            if (message.equals("Shutdown acknowledged") || Integer.parseInt(message) == R.string.ERROR_TCP_CLIENT) {
                Log.d(TAG, "Trying to reconnect... ");
                lastMsg.postValue("ERROR_TCP_CLIENT");
                TCPClientSet.getInstance().getClientByRobot(robot).stopClient();
                Thread.sleep(5000);
                TCPClientSet.getInstance().getClientByRobot(robot).startClient();
            }
        } catch (Exception exception) {
            try {
                lastMsg.postValue(message);
                TCPMessage tcpMessage = new TCPMessage(message);
                lastTCPMsg.postValue(tcpMessage);
                TCPClientSet.getInstance().getClientByRobot(robot).sendMessage(tcpMessage.getErrorCode() + "Received");
            } catch (Exception e) {
                TCPClientSet.getInstance().getClientByRobot(robot).sendMessage("Received Msg, but wrong format");
            }
        }
    }
}

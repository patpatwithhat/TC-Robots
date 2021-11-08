package com.example.tc_robots.ui.addrobotscreen;

import static com.example.tc_robots.uihelpers.InputValidation.isIPValid;
import static com.example.tc_robots.uihelpers.InputValidation.isPortValid;
import static com.example.tc_robots.uihelpers.InputValidation.isTextFieldValid;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.backend.network.TCPClient;
import com.example.tc_robots.backend.network.TCPClientSet;
import com.example.tc_robots.backend.tinyDB.TinySingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddRobotViewModel extends ViewModel {

    private static final String TAG = "AddRobotViewModel";


    private final MutableLiveData<List<Robot>> robots = new MutableLiveData<>(new ArrayList<>());
    private final Application application;

    public AddRobotViewModel(Application application) {
        this.application = application;
        initViewModelObservers();
    }

    public LiveData<List<Robot>> getRobots() {

        return robots;
    }

    public void initViewModelObservers() {
        robots.observeForever(robotList -> {
            List<Robot> allRobotsWithoutTCPClient = new ArrayList<>();
            outer:
            for (Robot robot : robotList) {
                for (TCPClient client : TCPClientSet.getInstance().getTcpClientList()) {
                    if (client.getRobot().equals(robot)) {
                        continue outer;
                    }
                }
                allRobotsWithoutTCPClient.add(robot);
            }
            allRobotsWithoutTCPClient.forEach(robot -> TCPClientSet.getInstance().createClient(robot).startClient());
            saveRobotsToSharedPref();
        });
    }

    public void getRunningRobots() {
        List<TCPClient> savedList = TCPClientSet.getInstance().getTcpClientList();
        savedList.forEach(tcpClient -> addRobot(tcpClient.getRobot()));
    }

    public void saveRobotsToSharedPref() {
        List<Robot> robotList = TCPClientSet.getInstance().getTcpClientList().stream().map(TCPClient::getRobot).collect(Collectors.toList());
        TinySingleton.getInstance().saveRobotsToTinyDB(robotList);
    }

    public void addAndSaveRobot(Robot robot) {
        addRobot(robot);
    }

    public void updateAndSaveRobot(Robot robot) {
        TCPClient client = TCPClientSet.getInstance().getClientByRobot(robot);
        client.restartClient();
        List<Robot> robotList = robots.getValue();
        robots.setValue(robotList);
    }


    private void addRobot(Robot robot) {
        List<Robot> robotList = robots.getValue();
        if (isRobotDuplicate(robot)) return;
        Objects.requireNonNull(robotList).add(robot);
        robots.setValue(robotList);
        Log.d(TAG, "Robot added: " + robot.getName());
    }

    public void removeRobot(Robot robot) {
        List<Robot> r = robots.getValue();
        Objects.requireNonNull(r).remove(robot);
        robots.setValue(r);
    }

    public boolean isRobotDuplicate(Robot robot) {
        return isRobotParamsDuplicate(robot) || isRobotNameDuplicate(robot);
    }

    public boolean isRobotDuplicate(Robot robot, List<Robot> list) {
        return isRobotParamsDuplicate(robot, list) || isRobotNameDuplicate(robot, list);
    }

    public boolean isRobotParamsDuplicate(Robot robot) {
        return isRobotParamsDuplicate(robot, Objects.requireNonNull(robots.getValue()));
    }

    public boolean isRobotParamsDuplicate(Robot robot, List<Robot> list) {
        return (Objects.requireNonNull(list).contains(robot));
    }

    public boolean isRobotNameDuplicate(Robot robot) {
        return isRobotNameDuplicate(robot, Objects.requireNonNull(robots.getValue()));
    }

    public boolean isRobotNameDuplicate(Robot robot, List<Robot> list) {
        List<Robot> robotNamesMatchList = list.stream().filter(robot1 -> robot1.getName().equals(robot.getName())).collect(Collectors.toList());
        return robotNamesMatchList.size() > 0;
    }

    public Robot getRobotDuplicate(Robot robot) {
        List<Robot> robotList = robots.getValue();
        int index = Objects.requireNonNull(robotList).indexOf(robot);
        return robotList.get(index);
    }

    public boolean isRobotNameValid(String name) {
        return !isTextFieldValid(name);
    }

    public boolean isRobotIPValid(String ip) {
        return isIPValid(ip);
    }

    public boolean isRobotPortValid(String port) {
        return isPortValid(port);
    }
}

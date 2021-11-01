package com.example.tc_robots.ui.addrobotscreen;

import static com.example.tc_robots.Constants.ROBOT_LIST_KEY;
import static com.example.tc_robots.uihelpers.InputValidation.isIPValid;
import static com.example.tc_robots.uihelpers.InputValidation.isPortValid;
import static com.example.tc_robots.uihelpers.InputValidation.isTextFieldValid;

import android.app.Application;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.backend.tinyDB.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddRobotViewModel extends ViewModel {

    private static final String TAG = "AddRobotViewModel";


    private final MutableLiveData<List<Robot>> robots = new MutableLiveData<>();
    private final Application application;
    private final TinyDB tinydb;

    public AddRobotViewModel(Application application) {
        this.application = application;
        robots.setValue(new ArrayList<>());
        tinydb = new TinyDB(application.getApplicationContext());
        // cleanSharedPref();
    }

    public LiveData<List<Robot>> getRobots() {

        return robots;
    }

    public void cleanSharedPref() {
        tinydb.clear();
    }

    public void getSavedRobots() {
        List<Object> listRobotObjects = tinydb.getListObject(ROBOT_LIST_KEY, Robot.class);
        listRobotObjects.forEach(robot -> addRobot((Robot) robot));
    }

    public void saveRobotsToSharedPref() {
        List<Robot> robotList = robots.getValue();
        ArrayList<Object> listRobotObjects = new ArrayList<Object>();
        Objects.requireNonNull(robotList).forEach(robot -> listRobotObjects.add((Object) robot));
        tinydb.putListObject(ROBOT_LIST_KEY, listRobotObjects);
    }

    public void addAndSaveRobot(Robot robot) {
        addRobot(robot);
        saveRobotsToSharedPref();
    }

    public void updateAndSaveRobot(Robot robot) {
        updateRobot(robot);
        saveRobotsToSharedPref();
    }

    private void updateRobot(Robot robot) {
        List<Robot> robotList = robots.getValue();
        String s = "";
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
        return isRobotParamsDuplicate(robot,  Objects.requireNonNull(robots.getValue()));
    }

    public boolean isRobotParamsDuplicate(Robot robot, List<Robot> list) {
        return (Objects.requireNonNull(list).contains(robot));
    }

    public boolean isRobotNameDuplicate(Robot robot) {
        return isRobotNameDuplicate(robot, Objects.requireNonNull(robots.getValue()));
    }
    public boolean isRobotNameDuplicate(Robot robot,List<Robot> list) {
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

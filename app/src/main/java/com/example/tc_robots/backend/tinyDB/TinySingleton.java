package com.example.tc_robots.backend.tinyDB;

import static com.example.tc_robots.Constants.ROBOT_LIST_KEY;

import android.app.Application;

import com.example.tc_robots.backend.monitoring.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TinySingleton {
    private static TinySingleton instance;

    private TinyDB tinydb;

    public static void initInstance(Application application) {
        if (instance == null) {
            // Create the instance
            instance = new TinySingleton(application);
        }
    }

    public static TinySingleton getInstance() {
        // Return the instance
        return instance;
    }

    private TinySingleton(Application application) {
        // Constructor hidden because this is a singleton
        tinydb = new TinyDB(application.getApplicationContext());
    }

    public List<Robot> getSavedRobots() {
        List<Object> listRobot = tinydb.getListObject(ROBOT_LIST_KEY, Robot.class);
        List<Robot> robots = new ArrayList<>();
        listRobot.forEach(robot -> robots.add((Robot) robot));
        return robots;
    }

    public void saveRobotsToTinyDB(List<Robot> robots) {
        ArrayList<Object> listRobotObj = new ArrayList<Object>();
        Objects.requireNonNull(robots).forEach(robot -> listRobotObj.add((Object) robot));
        tinydb.putListObject(ROBOT_LIST_KEY, listRobotObj);
    }

    public void cleanSharedPref() {
        tinydb.clear();
    }
}

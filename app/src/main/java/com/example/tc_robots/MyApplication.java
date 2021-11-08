package com.example.tc_robots;

import android.app.Application;
import android.content.res.Configuration;

import com.example.tc_robots.backend.network.TCPClientSet;
import com.example.tc_robots.backend.tinyDB.TinySingleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    ExecutorService executorService = Executors.newFixedThreadPool(5);

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        TinySingleton.initInstance(this);
       //TinySingleton.getInstance().cleanSharedPref();
        TCPClientSet.initInstance(executorService);
        TCPClientSet.getInstance().loadSavedRobotsFromSharedPref();
        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}

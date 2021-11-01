package com.example.tc_robots.ui.monitoringscreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tc_robots.ui.addrobotscreen.AddRobotViewModel;

public class MonitoringViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;


    public MonitoringViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MonitoringScreenViewModel(application);
    }
}

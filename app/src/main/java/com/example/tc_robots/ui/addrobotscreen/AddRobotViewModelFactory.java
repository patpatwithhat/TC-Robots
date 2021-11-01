package com.example.tc_robots.ui.addrobotscreen;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddRobotViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;


    public AddRobotViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddRobotViewModel(application);
    }

}
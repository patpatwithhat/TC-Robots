package com.example.tc_robots.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tc_robots.R;
import com.example.tc_robots.databinding.FragmentMonitoringscreenBinding;

public class MonitoringScreenFragment extends Fragment {
    FragmentMonitoringscreenBinding binding;

    MonitoringScreenViewModel viewModel;

    public MonitoringScreenFragment() {
        super(R.layout.fragment_monitoringscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMonitoringscreenBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_monitoringscreen, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MonitoringScreenViewModel.class);
        initUiElements();
    }

    private void initUiElements() {
    }


}

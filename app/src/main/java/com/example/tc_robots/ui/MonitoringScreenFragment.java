package com.example.tc_robots.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.TCPClient;
import com.example.tc_robots.databinding.FragmentMonitoringscreenBinding;
import com.example.tc_robots.uihelpers.CustomListAdapterAlerts;
import com.example.tc_robots.uihelpers.CustomListAdapterArticles;

public class MonitoringScreenFragment extends Fragment {
    private FragmentMonitoringscreenBinding binding;
    private static final String TAG = "MonitoringScreenFragment";
    
    private MonitoringScreenViewModel viewModel;
    private ListAdapter adapter;

    public MonitoringScreenFragment() {
        super(R.layout.fragment_monitoringscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
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
        viewModel.getAlertList().observe(getViewLifecycleOwner(), alerts -> {
            if (adapter == null) {
                adapter = new CustomListAdapterAlerts(MonitoringScreenFragment.this.requireContext(), alerts);
            }
            binding.listviewAlerts.setAdapter(adapter);
        });

    }


}

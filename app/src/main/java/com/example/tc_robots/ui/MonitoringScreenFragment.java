package com.example.tc_robots.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.Alert;
import com.example.tc_robots.backend.ErrorType;
import com.example.tc_robots.databinding.FragmentMonitoringscreenBinding;
import com.example.tc_robots.uihelpers.CustomListAdapterAlerts;

import java.util.List;

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
        viewModel.getAlertList().observe(getViewLifecycleOwner(), this::updateAdapterWithNewList);
        viewModel.getIsFilterActive().observe(getViewLifecycleOwner(), this::updateMenuBtnByErrorType);
        binding.btnShowAll.setOnClickListener(this::showAllAlerts);
        binding.btnFilterErrors.setOnClickListener(this::filterForErrors);
        binding.btnFilterWarnings.setOnClickListener(this::filterForWarnings);
        binding.btnFilterInfo.setOnClickListener(this::filterForInfos);
    }

    private void filterForErrors(View view) {
        List<Alert> filteredList = viewModel.filterForErrorTypeAndSetActiveErrorType(ErrorType.ERROR);
        updateAdapterWithNewList(filteredList);
    }

    private void filterForWarnings(View view) {
        List<Alert> filteredList = viewModel.filterForErrorTypeAndSetActiveErrorType(ErrorType.WARNING);
        updateAdapterWithNewList(filteredList);
    }

    private void filterForInfos(View view) {
        List<Alert> filteredList = viewModel.filterForErrorTypeAndSetActiveErrorType(ErrorType.INFO);
        updateAdapterWithNewList(filteredList);
    }

    private void showAllAlerts(View view) {
        List<Alert> filteredList = viewModel.getAllAlertsAndClearFilter();
        updateAdapterWithNewList(filteredList);
    }

    private void updateAdapterWithNewList(List<Alert> alertList) {
        adapter = new CustomListAdapterAlerts(MonitoringScreenFragment.this.requireContext(), alertList);
        binding.listviewAlerts.setAdapter(adapter);
    }

    private void updateMenuBtnByErrorType(Boolean isFilterActive) {
        if (isFilterActive && binding.btnShowAll.getBackground() != ResourcesCompat.getDrawable(getResources(), R.drawable.ic_exit_btn, null)) {
            binding.btnShowAll.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_exit_btn, null));
        } else if (!isFilterActive && binding.btnShowAll.getBackground() != ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_btn, null)) {
            binding.btnShowAll.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_btn, null));
        }
    }

}


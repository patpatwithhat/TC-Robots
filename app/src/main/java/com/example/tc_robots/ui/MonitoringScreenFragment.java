package com.example.tc_robots.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.Alert;
import com.example.tc_robots.backend.ErrorType;
import com.example.tc_robots.databinding.FragmentMonitoringscreenBinding;
import com.example.tc_robots.uihelpers.CustomListAdapterAlerts;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MonitoringScreenFragment extends Fragment {
    private FragmentMonitoringscreenBinding binding;
    private static final String TAG = "MonitoringScreenFragment";

    private MonitoringScreenViewModel viewModel;
    private ListAdapter adapter;

    //true = prev set to active     false = prev set to not active      initial true to trigger first change event
    private Boolean currentFilterStatus = true;

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
        updateMenuBtnByErrorType(false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initUiElements() {
        viewModel.getAlertList().observe(getViewLifecycleOwner(), this::updateAdapterWithNewList);
        viewModel.getIsFilterActive().observe(getViewLifecycleOwner(), this::updateMenuBtnByErrorType);
        binding.listviewAlerts.setOnTouchListener(this::onTouchListener);
        binding.btnShowAll.setOnClickListener(this::showAllAlerts);
        binding.btnFilterErrors.setOnClickListener(this::filterAlertsForErrors);
        binding.btnFilterWarnings.setOnClickListener(this::filterAlertsForWarnings);
        binding.btnFilterInfo.setOnClickListener(this::filterAlertsForInfos);
    }

    private void updateAdapterWithNewList(List<Alert> alertList) {
        adapter = new CustomListAdapterAlerts(MonitoringScreenFragment.this.requireContext(), alertList);
        binding.listviewAlerts.setAdapter(adapter);
    }

    //plays animation of Menu Button
    private void updateMenuBtnByErrorType(Boolean isFilterActive) {
        AnimatedVectorDrawableCompat drawable = null;
        if (isFilterActive && !currentFilterStatus) {
            drawable = AnimatedVectorDrawableCompat.create(this.requireContext(), R.drawable.menu_btn_animation_menu_to_close);
        } else if (!isFilterActive && currentFilterStatus) {
            drawable = AnimatedVectorDrawableCompat.create(this.requireContext(), R.drawable.menu_btn_animation_close_to_menu);
        }
        if (drawable != null) {
            binding.btnShowAll.setImageDrawable(drawable);
            drawable.setTint(ResourcesCompat.getColor(getResources(), R.color.primaryTextColor, null));
            drawable.start();
            currentFilterStatus = !currentFilterStatus;
        }
    }

    private boolean onTouchListener(View view, MotionEvent motionEvent) {
        new MaterialAlertDialogBuilder(this.requireContext(),R.style.Theme_TCRobots)
                .setTitle("title")
                .setMessage("message")
                .setPositiveButton("Stop Robot", (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                    view.getParent();
                    return;
                })
                .setNegativeButton("Ignore", (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                    return;
                })
                .setIcon(R.drawable.ic_launcher_foreground)
                .show();
        return true;
    }

    private void showAllAlerts(View view) {
        List<Alert> filteredList = viewModel.getAllAlertsAndClearFilter();
        updateAdapterWithNewList(filteredList);
    }

    private void filterAlertsForErrors(View view) {
        List<Alert> filteredList = viewModel.filterForErrorTypeAndSetActiveErrorType(ErrorType.ERROR);
        updateAdapterWithNewList(filteredList);
    }

    private void filterAlertsForWarnings(View view) {
        List<Alert> filteredList = viewModel.filterForErrorTypeAndSetActiveErrorType(ErrorType.WARNING);
        updateAdapterWithNewList(filteredList);
    }

    private void filterAlertsForInfos(View view) {
        List<Alert> filteredList = viewModel.filterForErrorTypeAndSetActiveErrorType(ErrorType.INFO);
        updateAdapterWithNewList(filteredList);
    }

}


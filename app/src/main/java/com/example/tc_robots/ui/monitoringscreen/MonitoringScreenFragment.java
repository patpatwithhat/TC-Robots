package com.example.tc_robots.ui.monitoringscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.monitoring.Alert;
import com.example.tc_robots.backend.monitoring.ErrorType;
import com.example.tc_robots.databinding.FragmentMonitoringscreenBinding;
import com.example.tc_robots.ui.MainActivity;
import com.example.tc_robots.ui.addrobot.AddRobotActivity;
import com.example.tc_robots.uihelpers.CustomListAdapterAlerts;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Objects;

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
        MainActivity activity = (MainActivity)getActivity();
        Objects.requireNonNull(activity).setTitle("Robot messages");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MonitoringScreenViewModel.class);
        setHasOptionsMenu(true);
        initUiElements();
        updateMenuBtnByErrorType(false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.requireActivity().getMenuInflater().inflate(R.menu.monitoring_menu, menu);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_robot:
                openAddRobotWindow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddRobotWindow() {
        Log.d(TAG,"addRoboClick");
        Intent i = new Intent(this.requireContext(), AddRobotActivity.class);
        startActivity(i);
        //Activity Intent
        //Bottom navigation ausblenden
        //add on Backstack
        //Hold activity status after first opening
        //json in project to edit custom ip/port pairs
    }

    private void initUiElements() {
        viewModel.getAlertList().observe(getViewLifecycleOwner(), this::updateAdapterWithNewList);
        viewModel.getIsFilterActive().observe(getViewLifecycleOwner(), this::updateMenuBtnByErrorType);
        binding.listviewAlerts.setOnItemClickListener(this::onListViewClick);
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

    //On ListView Element click show Alert Dialog
    private void onListViewClick(AdapterView<?> adapterView, View view, int position, long l) {

        List<Alert> alerts = viewModel.getAlertList().getValue();
        Alert alert = Objects.requireNonNull(alerts).get(position);

        new MaterialAlertDialogBuilder(adapterView.getContext())
                .setTitle(alert.getErrorCode())
                .setMessage(alert.getErrorText())
                .setPositiveButton("Stop Robot", (dialogInterface, i1) -> {

                })
                .setNegativeButton("Ignore Alert", (dialogInterface, i2) -> {
                })
                .setNeutralButton("Close",((dialogInterface, i3) ->{} ))
                .setIcon(R.drawable.ic_launcher_foreground)
                .show();
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


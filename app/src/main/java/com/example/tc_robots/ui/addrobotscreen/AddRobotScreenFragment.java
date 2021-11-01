package com.example.tc_robots.ui.addrobotscreen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tc_robots.MyApplication;
import com.example.tc_robots.R;
import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.databinding.AlertAddRobotBinding;
import com.example.tc_robots.databinding.FragmentAddRobotBinding;
import com.example.tc_robots.uihelpers.CustomListAdapterConnectedRobots;
import com.example.tc_robots.uihelpers.RobotAddDialog;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class AddRobotScreenFragment extends Fragment {

    private AddRobotViewModel viewModel;
    private FragmentAddRobotBinding binding;
    private ExecutorService executorService;

    public AddRobotScreenFragment() {
        super(R.layout.fragment_add_robot);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new AddRobotViewModelFactory(this.requireActivity().getApplication())).get(AddRobotViewModel.class);
        viewModel = new ViewModelProvider(this).get(AddRobotViewModel.class);
        binding = FragmentAddRobotBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyApplication application = (MyApplication) this.requireActivity().getApplication();
        executorService = application.getExecutorService();

        // initDataDummy();
        initUiElements();
        viewModel.getSavedRobots();
    }


    private void initUiElements() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        binding.btnAddRobot.setOnClickListener(this::onAddRobotClick);
        viewModel.getRobots().observe(this.getViewLifecycleOwner(), this::updateAdapterWithNewList);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void updateAdapterWithNewList(List<Robot> robots) {
        CustomListAdapterConnectedRobots adapter = new CustomListAdapterConnectedRobots(robots, viewModel);
        binding.recyclerView.setAdapter(adapter);
    }

    private void onAddRobotClick(View view) {
        createAlertDialogue();
    }

    private void createAlertDialogue() {
        RobotAddDialog robotAddDialog = new RobotAddDialog(requireContext());
        robotAddDialog.setViewModel(viewModel);
        robotAddDialog.createAndShow();
    }


}

package com.example.tc_robots.uihelpers;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.databinding.AlertAddRobotBinding;
import com.example.tc_robots.ui.addrobotscreen.AddRobotViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RobotManageDialog {
    private Context context;
    private AlertAddRobotBinding binding;
    private AddRobotViewModel viewModel;

    public RobotManageDialog(Context context) {
        this.context = context;
    }


    public Context getContext() {
        return context;
    }

    public void setViewModel(AddRobotViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void createAndShow() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), R.layout.alert_add_robot, null, false);
        AlertDialog dialog= new MaterialAlertDialogBuilder(this.getContext())
                .setView(binding.getRoot())
                .setTitle("Add a new device")
                .setPositiveButton("Add device", (dialogInterface, i) -> {
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .create();

        dialog.show();
       // dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
            if (checkAndSetInputErrors() && checkAndSetDuplicateError()) {
                viewModel.addAndSaveRobot((new Robot(getRobotName(),
                        getRobotIP(),
                        getRobotPort())));


                dialog.dismiss();
            }
        });
    }

    public void createAndShowForRobotUpdate(Robot robot, int positionOfOldEntry, OnRobotUpdated robotUpdated) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(this.getContext()), R.layout.alert_add_robot, null, false);
        AlertDialog dialog= new MaterialAlertDialogBuilder(this.getContext())
                .setView(binding.getRoot())
                .setPositiveButton("Update Robot", (dialogInterface, i) -> {
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
            if (checkAndSetInputErrors() && checkAndSetDuplicateError(updateRobot(robot), positionOfOldEntry)) {
                viewModel.updateAndSaveRobot(robot);
                robotUpdated.doOnRobotUpdated(positionOfOldEntry);
                dialog.dismiss();
            }
        });

        binding.tiRobotName.setText(robot.getName());
        binding.tiRobotIp.setText(robot.getIp());
        binding.tiRobotPort.setText(robot.getPort());
    }

    private Robot updateRobot(Robot robot) {
        robot.setName(getRobotName());
        robot.setIp(getRobotIP());
        robot.setPort(getRobotPort());
        return robot;
    }


    /**
     * @return true, if no input errors
     */
    private boolean checkAndSetInputErrors() {
        boolean hasNoErrors = true;
        if (!viewModel.isRobotNameValid(Objects.requireNonNull(binding.tiRobotName.getText()).toString())) {
            binding.tiRobotName.setError("Not a valid Name!");
            hasNoErrors = false;
        }
        if (!viewModel.isRobotIPValid(Objects.requireNonNull(binding.tiRobotIp.getText()).toString())) {
            binding.tiRobotIp.setError("Not a valid IP!");
            hasNoErrors = false;
        }
        if (!viewModel.isRobotPortValid(Objects.requireNonNull(binding.tiRobotPort.getText()).toString())) {
            binding.tiRobotPort.setError("Not a valid Port!");
            hasNoErrors = false;
        }
        return hasNoErrors;
    }

    /**
     * @return true, if no duplicate
     */
    private boolean checkAndSetDuplicateError() {
        boolean isParamDuplicate = checkIsRobotParamDuplicate();
        boolean isNameDuplicate = checkIsRobotNameDuplicate();
        if (isParamDuplicate) {
            Robot duplicate = viewModel.getRobotDuplicate(new Robot(getRobotName(), getRobotIP(), getRobotPort()));
            binding.errorText.setText("Sorry, this ip-port pair is already in use under the name: " + duplicate.getName());
        } else if (isNameDuplicate) {
            binding.errorText.setText("Sorry, a robot with this name already exists!");
        }
        return !isParamDuplicate && !isNameDuplicate;
    }
    /**
     * @return true, if no duplicate
     */
    private boolean checkAndSetDuplicateError(Robot robot,int positionOfOldEntry) {
        boolean isParamDuplicate = checkIsRobotParamDuplicate(robot,positionOfOldEntry);
        boolean isNameDuplicate = checkIsRobotNameDuplicate(robot,positionOfOldEntry);
        if (isParamDuplicate) {
            Robot duplicate = viewModel.getRobotDuplicate(new Robot(getRobotName(), getRobotIP(), getRobotPort()));
            binding.errorText.setText("Sorry, this ip-port pair is already in use under the name: " + duplicate.getName());
        } else if (isNameDuplicate) {
            binding.errorText.setText("Sorry, a robot with this name already exists!");
        }
        return !isParamDuplicate && !isNameDuplicate;
    }


    private boolean checkIsRobotNameDuplicate() {
        return viewModel.isRobotNameDuplicate(new Robot(getRobotName(), getRobotIP(), getRobotPort()));
    }

    private boolean checkIsRobotNameDuplicate(Robot robot,int positionOfOldEntry) {
        List<Robot> list = new ArrayList<>(Objects.requireNonNull(viewModel.getRobots().getValue()));
        Objects.requireNonNull(list).remove(positionOfOldEntry);
        return viewModel.isRobotNameDuplicate(robot, list);
    }

    private boolean checkIsRobotParamDuplicate() {
        return viewModel.isRobotParamsDuplicate(new Robot(getRobotName(), getRobotIP(), getRobotPort()));
    }

    private boolean checkIsRobotParamDuplicate(Robot robot,int positionOfOldEntry) {
        List<Robot> list = new ArrayList<>(Objects.requireNonNull(viewModel.getRobots().getValue()));
        Objects.requireNonNull(list).remove(positionOfOldEntry);
        return viewModel.isRobotParamsDuplicate(robot, list);
    }


    private String getRobotIP() {
        return Objects.requireNonNull(binding.tiRobotIp.getText()).toString().trim();
    }

    private String getRobotPort() {
        return Objects.requireNonNull(binding.tiRobotPort.getText()).toString().trim();
    }

    private String getRobotName() {
        return Objects.requireNonNull(binding.tiRobotName.getText()).toString().trim();
    }

    public interface OnRobotUpdated {
        void doOnRobotUpdated(int position);
    }

}

package com.example.tc_robots.uihelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.databinding.ListViewElementRobotBinding;
import com.example.tc_robots.ui.addrobotscreen.AddRobotViewModel;

import java.util.List;

public class CustomListAdapterConnectedRobots extends RecyclerView.Adapter<CustomListAdapterConnectedRobots.ViewHolder> {

    private final List<Robot> robotList;
    private AddRobotViewModel viewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListViewElementRobotBinding binding;

        public ViewHolder(ListViewElementRobotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Define click listener for the ViewHolder's View
        }

    }

    public CustomListAdapterConnectedRobots(List<Robot> localDataSet, AddRobotViewModel viewModel) {
        this.robotList = localDataSet;
        this.viewModel = viewModel;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        ListViewElementRobotBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.list_view_element_robot, viewGroup, false);
        return new ViewHolder(binding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Robot robot = robotList.get(position);
        viewHolder.binding.tvRobotName.setText(robot.getName());
        viewHolder.binding.btnRemoveRobot.setOnClickListener(view -> onRemoveClick(view, viewHolder.getAbsoluteAdapterPosition()));
        viewHolder.binding.lvElementRobot.setOnClickListener(view -> onListElementClick(view, viewHolder.getAbsoluteAdapterPosition()));
    }

    private void onListElementClick(View view, int position) {
        RobotAddDialog robotAddDialog = new RobotAddDialog(view.getContext());
        robotAddDialog.setViewModel(viewModel);
        robotAddDialog.createAndShowForRobotUpdate(this.robotList.get(position), position ,this::doOnRobotUpdated );
    }

    public void doOnRobotUpdated(int position) {
        notifyItemChanged(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return robotList.size();
    }

    private void onRemoveClick(View view, int position) {
        createDeleteDialogue(view.getContext(), position);
    }

    public void remove(int position) {
        this.robotList.remove(position);
        viewModel.saveRobotsToSharedPref();
        notifyItemRemoved(position);
    }

    private void createDeleteDialogue(Context context, int removeID) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Are you sure, you want to delete this device?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    remove(removeID);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .create();
        dialog.show();
    }

}

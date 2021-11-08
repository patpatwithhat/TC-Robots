package com.example.tc_robots.uihelpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.backend.network.TCPClient;
import com.example.tc_robots.backend.network.TCPClientSet;
import com.example.tc_robots.databinding.ListViewElementRobotBinding;
import com.example.tc_robots.ui.addrobotscreen.AddRobotViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.stream.Collectors;

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
        viewHolder.binding.tvIp.setText(robot.getIp());
        viewHolder.binding.tvPort.setText(robot.getPort());
        updateStatusIcon(viewHolder, robot);
        viewHolder.binding.btnRemoveRobot.setOnClickListener(view -> onRemoveClick(view, viewHolder.getAbsoluteAdapterPosition()));
        viewHolder.binding.lvElementRobot.setOnClickListener(view -> onListElementClick(view, viewHolder.getAbsoluteAdapterPosition()));
    }

    private void updateStatusIcon(ViewHolder viewHolder, Robot robot) {

        ShapeableImageView siv = new ShapeableImageView(viewHolder.itemView.getContext());
        Drawable drawable = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_cirlcle);
        siv.setBackgroundDrawable(drawable);
        viewHolder.binding.icon.setImageDrawable(drawable);

        List<TCPClient> clients = TCPClientSet.getInstance().getTcpClientList().stream().filter(client -> client.getRobot().equals(robot)).collect(Collectors.toList());
        if (clients.size() != 1) return;
        clients.get(0).addOnConnectionStatusChangeListener(new TCPClient.OnConnectionStatusChanged() {
            @Override
            public void connectionStatusChanged(Boolean status) {
                setIconColor(status, drawable, viewHolder.itemView.getContext());
            }
        });
        setIconColor(clients.get(0).isActive, drawable, viewHolder.itemView.getContext());
    }

    private void setIconColor(Boolean status, Drawable drawable, Context context) {
        if (status) {
            drawable.setTint(ResourcesCompat.getColor(context.getResources(), R.color.online_green, null));
        } else {
            drawable.setTint(ResourcesCompat.getColor(context.getResources(), R.color.error_red, null));
        }
    }

    private void onListElementClick(View view, int position) {
        RobotManageDialog robotManageDialog = new RobotManageDialog(view.getContext());
        robotManageDialog.setViewModel(viewModel);
        robotManageDialog.createAndShowForRobotUpdate(this.robotList.get(position), position, this::doOnRobotUpdated);
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
        Robot deletedRobot = this.robotList.remove(position);
        TCPClientSet.getInstance().deleteRobot(deletedRobot);
        viewModel.saveRobotsToSharedPref();
        notifyItemRemoved(position);
    }

    private void createDeleteDialogue(Context context, int removeID) {
        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle("Are you sure, you want to delete this device?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    remove(removeID);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .create();
        dialog.show();
    }

}

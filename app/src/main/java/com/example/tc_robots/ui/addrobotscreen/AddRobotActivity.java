package com.example.tc_robots.ui.addrobotscreen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tc_robots.MyApplication;
import com.example.tc_robots.R;
import com.example.tc_robots.backend.monitoring.Robot;
import com.example.tc_robots.databinding.ActivityAddRobotBinding;
import com.example.tc_robots.uihelpers.CustomListAdapterConnectedRobots;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class AddRobotActivity extends AppCompatActivity {
    private static final String TAG = "AddRobotActivity";
    ActivityAddRobotBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_robot);
        setTitle("Manage Robots");
    }


}

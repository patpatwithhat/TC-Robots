package com.example.tc_robots.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.ConnectTask;
import com.example.tc_robots.backend.TCPClient;
import com.example.tc_robots.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ActivityMainBinding binding;
    TCPClient client;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navControllerInit();
        new ConnectTask().execute("");
    }

    /**
     * Navigation Menu in MainActivity
     */
    private void navControllerInit() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        BottomNavigationView navigationView= binding.bottomNavigationView;
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setOnItemSelectedListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //check id
            case R.id.monitoringScreenFragment:
                Toast.makeText(this,"heress", Toast.LENGTH_LONG).show();
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.my_nav, true)
                        .build();
                Navigation.findNavController(this, R.id.fragmentContainerView).navigate(
                        R.id.monitoringScreenFragment,
                        null,
                        navOptions);
                break;
            case R.id.scannerScreenFragment:
                Navigation.findNavController(this, R.id.fragmentContainerView).navigate(R.id.scannerScreenFragment);
                Toast.makeText(this,"here", Toast.LENGTH_LONG).show();
                break;
        }
        item.setChecked(true);
        return true;
    }
}

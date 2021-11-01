package com.example.tc_robots.ui;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.tc_robots.MyApplication;
import com.example.tc_robots.R;
import com.example.tc_robots.backend.network.TCPClient;
import com.example.tc_robots.backend.network.TCPMessage;
import com.example.tc_robots.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;

import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, TCPClient.OnMessageReceived {

    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navControllerInit();
        initTCPClient();
    }

    public void initTCPClient() {
        MyApplication application = (MyApplication) this.getApplication();
        executorService = application.getExecutorService();
        TCPClient.getInstance().addOnMessageReceivedListener(this);
    }

    /**
     * Navigation Menu in MainActivity
     */
    private void navControllerInit() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        BottomNavigationView navigationView = binding.bottomNavigationView;
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
                break;
        }
        item.setChecked(true);
        return true;
    }

    private void sendToast(String message){
        Activity activity = this;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                MaterialTextView textView= findViewById(R.id.tv_status);
                textView.setText(message);
            }
        });
    }

    @Override
    public void messageReceived(String message) {
        sendToast(message);
        Log.d(TAG, "new message: " + message);
        try {
            if (Integer.parseInt(message) == R.string.ERROR_TCP_CLIENT) {
                sendToast("Roboter offline");
                Log.d(TAG, "Trying to reconnect... ");
                TCPClient.getInstance().stopClient();
                Thread.sleep(500);
                TCPClient.initInstance(executorService);
            }
        } catch (Exception exception) {
            try {
                TCPMessage tcpMessage = new TCPMessage(message);
                TCPClient.getInstance().sendMessage(tcpMessage.getErrorCode() + "Received");
            } catch (Exception e) {
                TCPClient.getInstance().sendMessage("Received Msg, but wrong format");
            }
        }

    }
}

package com.example.tc_robots.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.Customer;
import com.example.tc_robots.databinding.ActivityStatusscreenBinding;

import java.util.Objects;


public class StatusScreenActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityStatusscreenBinding binding;

    StatusScreenViewModel viewModel;
    Customer customer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusscreenBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(StatusScreenViewModel.class);
        initUiElements();
        getIntentInfo();
    }

    private void getIntentInfo() {
        if(getIntent().getExtras() != null) {
            customer = (Customer) getIntent().getSerializableExtra("customer");
        }
    }

    private void initUiElements() {
        initBtns();
    }

    private void initBtns() {
        binding.btnGatherOrder.setOnClickListener(this);
        binding.btnOrderAvailable.setOnClickListener(this);
        binding.btnSendOrderConfirmation.setOnClickListener(this);
        binding.btnSendShipment.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gather_order:
                break ;
            case R.id.btn_order_available:
                break ;
            case R.id.btn_send_order_confirmation:
                viewModel.sendOrderConfirmation(customer);
                break ;
            case R.id.btn_send_shipment:
                viewModel.sendShippingConfirmation(customer);
                break ;
            default:
                break;
        }
    }

}

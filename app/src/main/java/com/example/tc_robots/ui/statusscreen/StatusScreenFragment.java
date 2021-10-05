package com.example.tc_robots.ui.statusscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tc_robots.R;
import com.example.tc_robots.backend.Customer;
import com.example.tc_robots.databinding.FragmentStatusscreenBinding;


public class StatusScreenFragment extends Fragment implements View.OnClickListener {

    FragmentStatusscreenBinding binding;

    StatusScreenViewModel viewModel;
    Customer customer;

    public StatusScreenFragment() {
        super(R.layout.fragment_statusscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_statusscreen, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(StatusScreenViewModel.class);
        initUiElements();
        getIntentInfo();
    }


    private void getIntentInfo() {/*  if (getIntent().getExtras() != null) {
            customer = (Customer) getIntent().getSerializableExtra("customer");
        }*/

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
                break;
            case R.id.btn_order_available:
                break;
            case R.id.btn_send_order_confirmation:
                viewModel.sendOrderConfirmation(customer);
                break;
            case R.id.btn_send_shipment:
                viewModel.sendShippingConfirmation(customer);
                break;
            default:
                break;
        }
    }

}

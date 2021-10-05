package com.example.tc_robots.ui.orderscreen;

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
import com.example.tc_robots.databinding.FragmentOrderscreenBinding;

import java.util.Locale;
import java.util.Objects;

public class OrderScreenFragment extends Fragment implements View.OnClickListener {

    FragmentOrderscreenBinding binding;
    OrderScreenViewModel viewModel;

    public OrderScreenFragment() {
        super(R.layout.fragment_orderscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_orderscreen, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(OrderScreenViewModel.class);
        initUiElements();
    }


    private void initUiElements() {
        binding.btnOrdernow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Customer customer = new Customer(Objects.requireNonNull(binding.firstName.getEditText()).getText().toString().trim(),
                Objects.requireNonNull(binding.lastName.getEditText()).getText().toString().trim(),
                Objects.requireNonNull(binding.email.getEditText()).getText().toString().toLowerCase(Locale.ROOT).trim(),
                Objects.requireNonNull(binding.street.getEditText()).getText().toString().trim(),
                Objects.requireNonNull(binding.town.getEditText()).getText().toString().trim(),
                Objects.requireNonNull(binding.zipcode.getEditText()).getText().toString().trim());

        if (viewModel.isInputValid(customer)) {
            goToNextActivity(customer);
        }
    }

    private void goToNextActivity(Customer customer) {
       // Intent i = new Intent(OrderScreenFragment.this, StatusScreenFragment.class);
       // i.putExtra("customer", customer);
       // startActivity(i);
    }

}
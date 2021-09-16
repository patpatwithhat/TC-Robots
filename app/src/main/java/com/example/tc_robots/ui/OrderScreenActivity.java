package com.example.tc_robots.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.tc_robots.backend.Customer;
import com.example.tc_robots.backend.InputValidation;
import com.example.tc_robots.databinding.ActivityOrderscreenBinding;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class OrderScreenActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityOrderscreenBinding binding;
    OrderScreenViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderscreenBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(binding.getRoot());
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
        Intent i = new Intent(OrderScreenActivity.this,StatusScreenActivity.class);
        i.putExtra("customer", customer);
        startActivity(i);
    }

}
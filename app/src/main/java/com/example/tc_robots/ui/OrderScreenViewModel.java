package com.example.tc_robots.ui;

import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.Customer;
import com.example.tc_robots.backend.InputValidation;
import com.google.android.material.textfield.TextInputEditText;

public class OrderScreenViewModel extends ViewModel {


    public boolean isInputValid(Customer customer) {
        //Todo change for release
 /*       return InputValidation.isTextFieldValid(customer.getFirstName()) &&
                InputValidation.isTextFieldValid(customer.getLastName()) &&
                InputValidation.isEmailValid(customer.getEmail()) &&
                InputValidation.isTextFieldValid(customer.getStreet()) &&
                InputValidation.isTextFieldValid(customer.getTown()) &&
                InputValidation.isTextFieldValid(customer.getZipCode());*/
        return true;
    }

}

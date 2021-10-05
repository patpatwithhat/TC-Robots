package com.example.tc_robots.ui.statusscreen;

import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.Customer;

import papaya.in.sendmail.SendMail;

public class StatusScreenViewModel extends ViewModel {

    public void sendOrderConfirmation(Customer customer) {
        SendMail mail = new SendMail("tcrobotics.edu@gmail.com", "6ZtY6diG5gqNbn",
                customer.getEmail(),
                "Order Confirmation",
                "We received your order! \n Kind regards!");
        mail.execute();
    }

    public void sendShippingConfirmation(Customer customer) {
        SendMail mail = new SendMail("tcrobotics.edu@gmail.com", "6ZtY6diG5gqNbn",
                customer.getEmail(),
                "Shipping Confirmation",
                "Your articles are on the way! \n" +
                        "\n"+
                        customer.getFirstName() +" " + customer.getLastName() +"\n"+
                        customer.getStreet()+"\n"+
                        customer.getZipCode() + customer.getTown() +"\n"+
                        "\n"+
                        "Kind regards!");
        mail.execute();
    }
}

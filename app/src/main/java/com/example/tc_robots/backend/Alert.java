package com.example.tc_robots.backend;

import java.util.Date;

public class Alert {
    String errorCode, errorText; //maybe extra error class containing both
    CustomDate date;

    public Alert(String errorCode, String errorText, CustomDate date) {
        this.errorCode = errorCode;
        this.errorText = errorText;
        this.date = date;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public CustomDate getDate() {
        return date;
    }

}

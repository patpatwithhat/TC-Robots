package com.example.tc_robots.backend.monitoring;

import com.example.tc_robots.backend.monitoring.CustomDate;
import com.example.tc_robots.backend.monitoring.ErrorType;

public class Alert {
    ErrorType errorType;
    String errorCode, errorText; //maybe extra error class containing both
    CustomDate date;

    public Alert(ErrorType errorType, String errorCode, String errorText, CustomDate date) {
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorText = errorText;
        this.date = date;
    }

    public Alert(ErrorType errorType, String errorCode, String errorText) {
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorText = errorText;
        this.date = new CustomDate();
    }

    public Alert(String rawString) {
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorText = errorText;
        this.date = new CustomDate();
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
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

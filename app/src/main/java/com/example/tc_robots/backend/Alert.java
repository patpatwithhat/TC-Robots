package com.example.tc_robots.backend;

import java.util.Date;

public class Alert {
    String errorCode, errorText; //maybe extra error class containing both
    Long date;

    public Alert(String errorCode, String errorText, Long date) {
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}

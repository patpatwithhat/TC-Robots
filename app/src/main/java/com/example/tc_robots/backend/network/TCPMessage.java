package com.example.tc_robots.backend.network;

public class TCPMessage {
    String errorCode;
    String confirmationText;
    int metaInfo;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getConfirmationText() {
        return confirmationText;
    }

    public void setConfirmationText(String confirmationText) {
        this.confirmationText = confirmationText;
    }

    public int getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(int metaInfo) {
        this.metaInfo = metaInfo;
    }

    //rawMessage: 1234_Temperatur4455
    //4nr_10char4nr
    public TCPMessage(String rawMessage) {
        errorCode= rawMessage.substring(0,4);
        confirmationText = rawMessage.substring(5,15);
        metaInfo = Integer.parseInt(rawMessage.substring(15));
    }
}

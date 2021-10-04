package com.example.tc_robots.backend;

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

    //rawMessage: 123_Temperat4455
    //3nr_8char4nr
    public TCPMessage(String rawMessage) {
        errorCode= rawMessage.substring(0,3);
        confirmationText = rawMessage.substring(4,12);
        metaInfo = Integer.parseInt(rawMessage.substring(12));
    }
}

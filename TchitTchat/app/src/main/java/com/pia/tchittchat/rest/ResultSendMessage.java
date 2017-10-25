package com.pia.tchittchat.rest;

/**
 * Created by pia92 on 25/10/2017.
 */

public class ResultSendMessage {

    public int status;
    public String message;

    public ResultSendMessage() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

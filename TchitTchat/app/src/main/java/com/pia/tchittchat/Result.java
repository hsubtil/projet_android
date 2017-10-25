package com.pia.tchittchat;

public class Result {
    public int status;
    public String message;
    public String[] element;

    public Result() {
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

    public String[] getElement() {
        return element;
    }

    public void setElement(String[] element) {
        this.element = element;
    }
}

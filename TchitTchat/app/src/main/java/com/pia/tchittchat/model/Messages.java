package com.pia.tchittchat.model;

import java.util.ArrayList;

/**
 * Created by pia92 on 25/10/2017.
 */

public class Messages {
    String uuid;
    String login;
    String message;
    Attachment attachments;

    public Attachment getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachment attachments) {
        this.attachments = attachments;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

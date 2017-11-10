package com.pia.tchittchat.model;

/**
 * Created by Hugo on 27/10/2017.
 */

public class Profile {
    private String login;
    private String picture;
    private String email;


    public Profile() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}

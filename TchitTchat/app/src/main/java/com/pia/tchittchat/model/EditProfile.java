package com.pia.tchittchat.model;

/**
 * Created by Hugo on 10/11/2017.
 */

public class EditProfile {
    public String password;
    public String picture;
    public String email;

    public EditProfile(){

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

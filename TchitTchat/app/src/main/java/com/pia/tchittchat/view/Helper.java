package com.pia.tchittchat.view;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Hugo on 26/10/2017.
 */

public class Helper {

    public static String createAuthToken(String username,String password){
        byte[] data = new byte[0];
        try {
            data = (username + ":" + password).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String output = "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
        return output;
    }

}

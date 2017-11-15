package com.pia.tchittchat.rest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Hugo on 15/11/2017.
 */

public class NetworkCom {
    private static Socket mSocket;
    private Manager.Options options;
    private Manager mManager;

    public NetworkCom(){
        this.options = new Manager.Options();
        options.path = "/chat-rest/socket.io";
        URI uri = URI.create("https://training.loicortola.com/");
        this.mManager = new Manager(uri, options);
        this.mSocket = mManager.socket("/2.0/ws");

        this.mSocket.connect();
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void emitConnect(String login,String password){
        JSONObject json = new JSONObject();
        try {
            json.put("login", login);
            json.put("password",password);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("auth_attempt",json);
    }

    public void destroySocket() {
        mSocket.disconnect();
    }



}

package com.pia.tchittchat;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Hugo on 25/10/2017.
 */

public class MyApplication extends Application {
    private ConnectionManager connectionService;

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionManager.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        connectionService = retrofit.create(ConnectionManager.class);
    }

    public ConnectionManager getConnectionService() {
        return connectionService;
    }
}

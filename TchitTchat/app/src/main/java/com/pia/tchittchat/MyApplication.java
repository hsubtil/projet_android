package com.pia.tchittchat;

import android.app.Application;

import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.ApiManager2_0;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Hugo on 25/10/2017.
 */

public class MyApplication extends Application {
    private ApiManager1_0 apiManager1_0;
    private ApiManager2_0 apiManager2_0;
//    private OkHttpClient okHttpClient;


    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiManager1_0.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiManager1_0 = retrofit.create(ApiManager1_0.class);

        Retrofit retrofit2_0 = new Retrofit.Builder()
                .baseUrl(ApiManager2_0.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiManager2_0 = retrofit2_0.create(ApiManager2_0.class);
    }

    public ApiManager1_0 getApiManager1_0() {
        return apiManager1_0;
    }

    public ApiManager2_0 getApiManager2_0() {
        return apiManager2_0;
    }
}

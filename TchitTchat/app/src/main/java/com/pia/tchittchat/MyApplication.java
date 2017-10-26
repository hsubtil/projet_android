package com.pia.tchittchat;

import android.app.Application;

import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.ApiManager2_0;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Hugo on 25/10/2017.
 */

public class MyApplication extends Application {
    private ApiManager1_0 apiManager1_0;
    private ApiManager2_0 apiManager2_0;
    private OkHttpClient okHttpClient;


    @Override
    public void onCreate() {
        super.onCreate();
        okHttpClient = buildClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiManager1_0.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiManager1_0 = retrofit.create(ApiManager1_0.class);

        Retrofit retrofit2_0 = new Retrofit.Builder()
                .baseUrl(ApiManager2_0.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiManager2_0 = retrofit2_0.create(ApiManager2_0.class);
    }

    public ApiManager1_0 getApiManager1_0() {
        return apiManager1_0;
    }

    public ApiManager2_0 getApiManager2_0() {
        return apiManager2_0;
    }
    private OkHttpClient buildClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                return response;
            }
        });

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //this is where we will add whatever we want to our request headers.
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }
        });

        return builder.build();
    }
}

package com.pia.tchittchat.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Pia on 10/20/2017.
 */


public interface ConnectionManager {

    String ENDPOINT = "https://training.loicortola.com/chat-rest/1.0/";

    @GET("connect/{login}/{password}")
    Call<Result> getUser(@Path("login") String login, @Path("password") String password);

    @POST("register/{login}/{password}")
    Call<Result> registerUser(@Path("login") String login, @Path("password") String password);

}


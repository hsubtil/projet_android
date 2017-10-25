package com.pia.tchittchat.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Pia on 10/20/2017.
 */


public interface ConnectionManager {

    public static final String ENDPOINT = "https://training.loicortola.com/chat-rest/1.0/";

    @GET("connect/{login}/{password}")
    Call<Result> getUser(@Path("login") String login, @Path("password") String password);

}


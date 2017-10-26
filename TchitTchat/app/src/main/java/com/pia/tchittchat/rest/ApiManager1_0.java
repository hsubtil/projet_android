package com.pia.tchittchat.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Hugo on 26/10/2017.
 */

public interface ApiManager1_0 {

    String ENDPOINT = "https://training.loicortola.com/chat-rest/1.0/";

    @GET("connect/{login}/{password}")
    Call<Result> getUser(@Path("login") String login, @Path("password") String password);

    @POST("register/{login}/{password}")
    Call<Result> registerUser(@Path("login") String login, @Path("password") String password);

    @GET("messages/{login}/{password}")
    Call<List<ResultGetMessage>> getMessages(@Path("login") String login, @Path("password") String password);

    @POST("messages/{login}/{password}")
    Call<ResultSendMessage> sendMessage(@Path("login") String login, @Path("password") String password, @Body ResultGetMessage resultGetMessage);


}
package com.pia.tchittchat.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Hugo on 26/10/2017.
 */

public interface ApiManager2_0 {

    String ENDPOINT = "https://training.loicortola.com/chat-rest/2.0/";

    @GET("connect")
    Call<Result> connect(@Header("Authorization") String authorization);
    @GET("messages")
    Call<List<ResultGetMessage>> messages(@Header("Authorization") String authorization, @Query("limit") Integer limit, @Query("offset") Integer offeset);
/*
    @POST("register/{login}/{password}")
    Call<Result> registerUser(@Path("login") String login, @Path("password") String password);

    @GET("messages/{login}/{password}")
    Call<List<ResultGetMessage>> getMessages(@Path("login") String login, @Path("password") String password);

    @POST("messages/{login}/{password}")
    Call<ResultSendMessage> sendMessage(@Path("login") String login, @Path("password") String password, @Body ResultGetMessage messageElement);
*/

}
package com.pia.tchittchat;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;



/**
 * Created by Pia on 10/20/2017.
 */


public interface ConnectionManager {

    public static final String ENDPOINT = "https://training.loicortola.com/chat-rest/1.0";

    @GET("/connect/{login}/{password}")
    Result getUser(@Path("login") String login, @Path("password") String password);

}


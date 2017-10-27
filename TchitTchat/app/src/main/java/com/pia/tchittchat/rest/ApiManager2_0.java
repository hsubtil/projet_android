package com.pia.tchittchat.rest;

import android.widget.ImageView;

import com.pia.tchittchat.model.Auth;
import com.pia.tchittchat.model.Messages;
import com.pia.tchittchat.model.Result;
import com.pia.tchittchat.model.ResultMessages;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Hugo on 26/10/2017.
 */

public interface ApiManager2_0 {

    String ENDPOINT = "https://training.loicortola.com/chat-rest/2.0/";

    @GET("connect")
    Call<Result> connect(@Header("Authorization") String authorization);

    @GET("messages")
    Call<List<Messages>> getMessages(@Header("Authorization") String authorization, @Query("limit") Integer limit, @Query("offset") Integer offeset);

    @POST("messages")
    Call<ResultMessages> sendMessages(@Header("Authorization") String authorization, @Body Messages messageElement);
    
    @POST("register")
    Call<Result> registerUser( @Body Auth resultAuth );

    @GET("files")
    Call <ImageView> getAttachements (@Header("Authorization") String authorization, @Query( "uuid") String uuid, @Query("filename") String filename);

}
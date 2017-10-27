package com.pia.tchittchat.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.model.Profile;
import com.pia.tchittchat.model.Result;
import com.pia.tchittchat.rest.ApiManager2_0;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ApiManager2_0 apiManager2_0;
    private SharedPreferences mPrefs;
    private TextView loginView;
    private TextView mailView;
    String login;
    //TODO : Add image handling
    // TODO: Edit profile (Password, email, picture)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiManager2_0 = ((MyApplication) getApplication()).getApiManager2_0();
        mPrefs = getSharedPreferences("authToken", 0);

        //View Elements
        loginView = (TextView) findViewById(R.id.profileLogin);
        mailView = (TextView) findViewById(R.id.profileMail);

        String authToken = mPrefs.getString("authToken", "null");
        login = getIntent().getStringExtra("LOGIN");
        Call<Profile> call = apiManager2_0.getProfile(authToken,login);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.body() != null) {
                    loginView.setText(response.body().login);
                    mailView.setText(response.body().email);
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}


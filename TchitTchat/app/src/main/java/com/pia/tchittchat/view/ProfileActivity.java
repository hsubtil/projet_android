package com.pia.tchittchat.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private TextView editProfile;
    String login;
    //TODO : Add image handling

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiManager2_0 = ((MyApplication) getApplication()).getApiManager2_0();
        mPrefs = getSharedPreferences("authToken", 0);

        //View Elements
        loginView = (TextView) findViewById(R.id.profileLogin);
        mailView = (TextView) findViewById(R.id.profileMail);
        editProfile = (Button) findViewById(R.id.editProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intentEditProfile.putExtra("MAIL", mailView.getText().toString());
                startActivity(intentEditProfile);
            }
        });

        String authToken = mPrefs.getString("authToken", "null");
        login = mPrefs.getString("authLogin", "null");
        //login = getIntent().getStringExtra("LOGIN");
        Call<Profile> call = apiManager2_0.getProfile(authToken, login);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.body() != null) {
                    loginView.setText(response.body().getLogin());
                    mailView.setText(response.body().getEmail());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}


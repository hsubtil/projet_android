package com.pia.tchittchat.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.model.Image;
import com.pia.tchittchat.model.Messages;
import com.pia.tchittchat.model.Profile;
import com.pia.tchittchat.model.Result;
import com.pia.tchittchat.rest.ApiManager2_0;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ApiManager2_0 apiManager2_0;
    private SharedPreferences mPrefs;
    private TextView loginView;
    private TextView mailView;
    private TextView editProfile;
    private ImageView edit_profile_current_picture;
    String login;

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
        edit_profile_current_picture = (ImageView) findViewById(R.id.edit_profile_current_picture);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intentEditProfile.putExtra("MAIL", mailView.getText().toString());
                startActivity(intentEditProfile);
            }
        });

        final String authToken = mPrefs.getString("authToken", "null");
        login = mPrefs.getString("authLogin", "null");
        //login = getIntent().getStringExtra("LOGIN");
        Call<Profile> call = apiManager2_0.getProfile(authToken, login);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.body() != null) {
                    getProfilePictures(response.body(), edit_profile_current_picture,authToken);
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

    public void getProfilePictures(Profile profil, final ImageView edit_profile_current_picture, String authToken) {
        String profilPicture = profil.getPicture();
        String fileName = "";

        if (profilPicture != null) {
            String[] filePath = profilPicture.split("/");
            fileName = filePath[filePath.length - 1];

            Call<ResponseBody> call = apiManager2_0.getProfilePicture(authToken, fileName);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Glide.with(ProfileActivity.this)
                                .load(response.body().bytes())
                                .asBitmap()
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(edit_profile_current_picture);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


}


package com.pia.tchittchat.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.model.EditProfile;
import com.pia.tchittchat.model.Profile;
import com.pia.tchittchat.model.ResultMessages;
import com.pia.tchittchat.rest.ApiManager2_0;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private Button editProfileButton;
    private TextView newPassword;
    private TextView newPasswordVerif;
    private TextView newEmail;
    private SharedPreferences mPrefs;
    private String mail;

    private ApiManager2_0 apiManager2_0;
    // TODO: Profile img edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfileButton = (Button) findViewById(R.id.edit_profile_submit);
        newPassword = (TextView) findViewById(R.id.edit_profile_password);
        newPasswordVerif = (TextView) findViewById(R.id.edit_profile_password_verif);
        newEmail = (TextView) findViewById(R.id.edit_profile_mail);

        mail = getIntent().getStringExtra("MAIL");


        apiManager2_0 =  ((MyApplication) getApplication()).getApiManager2_0();
        mPrefs = getSharedPreferences("authToken", 0);


        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile newProfile = new EditProfile();
                //Check double password
                //Check if empty
                checkMail(newProfile);
                checkPassword(newProfile);
                // Send new profile
                String authToken = mPrefs.getString("authToken", "null");
                Call<ResultMessages> call = apiManager2_0.editProfile(authToken,newProfile);
                call.enqueue(new Callback<ResultMessages>() {
                    @Override
                    public void onResponse(Call<ResultMessages> call, Response<ResultMessages> response) {
                        if (response.body() != null) {
                            if (response.code() == 200) {
                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                Intent intentLogged = new Intent(EditProfileActivity.this, LoginActivity.class);
                                startActivity(intentLogged);
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Something went wrong try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultMessages> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });
    }

    private void checkMail(EditProfile newProfile){
        if(newEmail.getText().length()==0) {
            newProfile.setEmail(mail);// Change nothing
        }
        else {
            newProfile.setEmail(newEmail.getText().toString());
        }
    }

    private void checkPassword(EditProfile newProfile){
        if(newPassword.getText().length()==0) {
            // Change nothing
        }
        else {
            if (newPassword.getText().toString().equals(newPasswordVerif.getText().toString())) {
                newProfile.setPassword(newPassword.getText().toString());
            }
        }
    }



}

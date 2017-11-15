package com.pia.tchittchat.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.model.Profile;
import com.pia.tchittchat.rest.ApiManager2_0;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {

    private ApiManager2_0 apiManager2_0;
    private SharedPreferences mPrefs;
    private TextView loginView;
    private TextView mailView;
    private Button searchbtn;
    private EditText login;
    private ImageView contact_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        apiManager2_0 = ((MyApplication) getApplication()).getApiManager2_0();
        mPrefs = getSharedPreferences("authToken", 0);

        //View Elements
        loginView = (TextView) findViewById(R.id.contactLogin);
        mailView = (TextView) findViewById(R.id.contactMail);
        searchbtn = (Button) findViewById(R.id.contactSearchBtn);
        login = (EditText) findViewById(R.id.contactSearch);
        contact_picture = (ImageView) findViewById(R.id.contact_picture);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String authToken = mPrefs.getString("authToken", "null");
                Call<Profile> call = apiManager2_0.getProfile(authToken,login.getText().toString());
                call.enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(Call<Profile> call, Response<Profile> response) {
                        if (response.body() != null) {
                            getProfilePictures(response.body(), contact_picture,authToken);
                            loginView.setText(response.body().getLogin());
                            mailView.setText(response.body().getEmail());
                        }
                        else{
                            Toast.makeText(ContactActivity.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Profile> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
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
                        Glide.with(ContactActivity.this)
                                .load(response.body().bytes())
                                .asBitmap()
                                .placeholder(R.drawable.border)
                                .into(contact_picture);

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

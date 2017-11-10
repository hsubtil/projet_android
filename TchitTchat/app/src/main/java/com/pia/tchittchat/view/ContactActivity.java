package com.pia.tchittchat.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.model.Profile;
import com.pia.tchittchat.rest.ApiManager2_0;

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
    //TODO : Add image handling

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

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authToken = mPrefs.getString("authToken", "null");
                Call<Profile> call = apiManager2_0.getProfile(authToken,login.getText().toString());
                call.enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(Call<Profile> call, Response<Profile> response) {
                        if (response.body() != null) {
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
}

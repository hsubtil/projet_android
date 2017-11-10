package com.pia.tchittchat.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.ApiManager2_0;
import com.pia.tchittchat.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button submitBtn;
    private Button signUpBtn ;
    private EditText username;
    private EditText password;
    private ProgressBar progressBar;
    private SharedPreferences mPrefs;
    private ApiManager2_0 apiManager2_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPrefs = getSharedPreferences("authToken", 0);

//        ConnectionManager connect = new ConnectionManager();
//        InputStream inputStream = connect.getConnection();

        // Api for get/post requests
        //apiManager = ((MyApplication) getApplication()).getApiManager1_0();
        apiManager2_0 =  ((MyApplication) getApplication()).getApiManager2_0();

        // TODO : Change Token and put a timeout on it.
        checkConnectionToken();

        submitBtn.setOnClickListener ( new View.OnClickListener (){
            @Override
            public void onClick (View v){
                progressBar.setVisibility(View.VISIBLE);
               // Call<Result> call = apiManager.getUser(username.getText().toString(), password.getText().toString());

               // String mString = mPrefs.getString("tag", "default_value_if_variable_not_found");
                SharedPreferences.Editor mEditor = mPrefs.edit();
                String authToken = Helper.createAuthToken(username.getText().toString(), password.getText().toString());
                mEditor.putString("authToken", authToken).apply();
                Call<Result> call = apiManager2_0.connectWithAuth(authToken);
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if(response.body()!=null)
                        {
                            if(response.body().status == 200){
                            Toast.makeText(LoginActivity.this,"Hello",Toast.LENGTH_LONG).show();
                            Intent intentLogged = new Intent(LoginActivity.this, MainActivity.class);
                            intentLogged.putExtra("USERNAME", username.getText().toString());
                            intentLogged.putExtra("PASSWORD", password.getText().toString());
                                startActivity(intentLogged);
                            }

                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Login or password incorrect",Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intentSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentSignUp);
            }
        });


    }

    private void checkConnectionToken(){
        String authToken = mPrefs.getString("authToken", "null");
        Call<Result> call = apiManager2_0.connectWithAuth(authToken);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                    if (response.body().status == 200) {
                        Toast.makeText(LoginActivity.this, "Hello again", Toast.LENGTH_LONG).show();
                        Intent intentalredyLogged = new Intent(LoginActivity.this, MainActivity.class);
                        intentalredyLogged.putExtra("USERNAME", "hugo");
                        intentalredyLogged.putExtra("PASSWORD", "hugo");
                        startActivity(intentalredyLogged);
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Authentication timeout", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}


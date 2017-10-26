package com.pia.tchittchat.model;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.ApiManager2_0;
import com.pia.tchittchat.rest.Result;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button submitBtn;
    private Button signUpBtn ;
    private EditText username;
    private EditText password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//        ConnectionManager connect = new ConnectionManager();
//        InputStream inputStream = connect.getConnection();

        final ApiManager1_0 apiManager = ((MyApplication) getApplication()).getApiManager1_0();
        final ApiManager2_0 apiManager2_0 = ((MyApplication) getApplication()).getApiManager2_0();


        submitBtn.setOnClickListener ( new View.OnClickListener (){
            @Override
            public void onClick (View v){
                progressBar.setVisibility(View.VISIBLE);
               // Call<Result> call = apiManager.getUser(username.getText().toString(), password.getText().toString());
                Call<Result> call = apiManager2_0.connect(getAuthToken());
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
    public String getAuthToken(){
        byte[] data = new byte[0];
        try {
            data = (username.getText().toString() + ":" + password.getText().toString()).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String output = "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
        return output;
    }

}


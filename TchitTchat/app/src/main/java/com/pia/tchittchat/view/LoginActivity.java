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
import com.pia.tchittchat.rest.NetworkCom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button submitBtn;
    private Button signUpBtn;
    private EditText username;
    private EditText password;
    private ProgressBar progressBar;
    private SharedPreferences mPrefs;
    private ApiManager2_0 apiManager2_0;
    private NetworkCom socket;

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


        // Api for get/post requests
        //apiManager = ((MyApplication) getApplication()).getApiManager1_0();
        apiManager2_0 = ((MyApplication) getApplication()).getApiManager2_0();

        //Socket
        socket = new NetworkCom();
        socket.getmSocket().on("auth_success",onAuthSuccess);
        socket.getmSocket().on("auth_failed",onAuthFail);



        checkConnectionToken();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setProgressBarVisible();

                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putLong("lastLogin", new Date().getTime());                 // Add a time to check timeout
                mEditor.putString("authLogin",username.getText().toString());     // Add Login
                mEditor.commit();
                socket.emitConnect(username.getText().toString(),password.getText().toString());
                Toast.makeText(LoginActivity.this, "Authentication Failed. Please try again.", Toast.LENGTH_LONG).show();
               // progressBar.setVisibility(View.INVISIBLE);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentSignUp);
            }
        });


    }

    private void checkConnectionToken() {
        Long lastLogin = mPrefs.getLong("lastLogin", 36);
        long acutallogin = new Date().getTime();
        if (lastLogin != 0 && (acutallogin - lastLogin) < 10000) {

            Toast.makeText(LoginActivity.this, "Hello again !", Toast.LENGTH_LONG).show();
            Intent intentLogged = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentLogged);
            /*Call<Result> call = apiManager2_0.connectWithAuth(authToken);
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
            });*/
        }
        else{
            Toast.makeText(LoginActivity.this, "Authentication timeout", Toast.LENGTH_LONG).show();
        }

    }

    private Emitter.Listener onAuthSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //TODO
            // 	Payload: {"login":"foo","token":"session-token1234","message": "Authentication successful"}
            JSONObject obk = (JSONObject) args[0];

            String token = "";
            try {
                token = obk.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor mEditor = mPrefs.edit();
            // Shared preference declaration
            mEditor.putString("sessionToken", token).apply();
            String authToken = Helper.createAuthToken(username.getText().toString(),password.getText().toString());
            mEditor.putString("authToken", authToken).apply(); // Create / update TOKEN

            Toast.makeText(LoginActivity.this, "Hello", Toast.LENGTH_LONG).show();
            Intent intentLogged = new Intent(LoginActivity.this, MainActivity.class);
            intentLogged.putExtra("USERNAME", username.getText().toString());
            intentLogged.putExtra("PASSWORD", password.getText().toString());
            startActivity(intentLogged);

            return;
        }
    };


    private Emitter.Listener onAuthFail = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            return;
        }
    };

    private void setProgressBarInvisible(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setProgressBarVisible(){
        progressBar.setVisibility(View.VISIBLE);
    }

}


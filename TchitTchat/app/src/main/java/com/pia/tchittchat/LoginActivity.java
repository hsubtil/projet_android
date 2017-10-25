package com.pia.tchittchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import static android.R.id.message;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    Button submitBtn;
    Button signUpBtn ;
    EditText username;
    EditText password;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//        ConnectionManager connect = new ConnectionManager();
//        InputStream inputStream = connect.getConnection();

        super.onCreate(savedInstanceState);

        ;

        submitBtn.setOnClickListener ( new View.OnClickListener (){
            @Override
            public void onClick (View v){
                    new  LoginTask().execute();
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

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            //Show progress bar
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }


        protected void onPostExecute(Boolean success) {
            Toast.makeText(LoginActivity.this,"Hello",Toast.LENGTH_LONG).show();
            if(success)
            {  Intent intentLogged = new Intent(LoginActivity.this, MainActivity.class);
                intentLogged.putExtra("USERNAME", username.getText().toString());
               startActivity(intentLogged);
            }
            else {
                Toast.makeText(LoginActivity.this,"Wrong",Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}


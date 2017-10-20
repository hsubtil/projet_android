package com.pia.tchittchat;

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
                //TODO : Redirect the page to SignUp page
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
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("https://training.loicortola.com/chat-rest/1.0");
                urlConnection = (HttpURLConnection) url.openConnection();
            }  catch (MalformedURLException e1) {
                e1.printStackTrace();
            }catch (IOException e2) {
                e2.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }


            return true;
        }


        protected void onPostExecute(Boolean success) {
            if(success)
            {
                Toast.makeText(LoginActivity.this,"Welcome",Toast.LENGTH_LONG).show();
               // Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                //startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this,"Wrong",Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}


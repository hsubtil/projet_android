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

public class LoginActivity extends AppCompatActivity {
    Button submitBtn;
    Button resetBtn ;
    EditText username;
    EditText password;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        super.onCreate(savedInstanceState);

        ;

        submitBtn.setOnClickListener ( new View.OnClickListener (){
            @Override
            public void onClick (View v){
                new  LoginTask().execute();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                username.setText("");
                password.setText("");

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
            //Do heavy lifting here
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            return true;
        }


        protected void onPostExecute(Boolean success) {
            if(success)
            {
                Toast.makeText(LoginActivity.this,"Hello",Toast.LENGTH_LONG).show();
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


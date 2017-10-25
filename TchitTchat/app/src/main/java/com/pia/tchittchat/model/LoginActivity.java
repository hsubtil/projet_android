package com.pia.tchittchat.model;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ConnectionManager;
import com.pia.tchittchat.rest.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button submitBtn;
    Button signUpBtn ;
    EditText username;
    EditText password;
    ProgressBar progressBar;
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

        final ConnectionManager connectionManager = ((MyApplication) getApplication()).getConnectionService();


        ;

        submitBtn.setOnClickListener ( new View.OnClickListener (){
            @Override
            public void onClick (View v){
                Call<Result> call = connectionManager.getUser(username.getText().toString(), password.getText().toString());
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if(response.body().status==200)
                        {
                            Toast.makeText(LoginActivity.this,"Hello",Toast.LENGTH_LONG).show();
                            Intent intentLogged = new Intent(LoginActivity.this, MainActivity.class);
                            intentLogged.putExtra("USERNAME", username.getText().toString());
                            startActivity(intentLogged);
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Wrong",Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {

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

//    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
//        @Override
//        protected void onPreExecute() {
//            //Show progress bar
//            progressBar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            new QueryUser().execute("hugo","hugo");
//            return true;
//        }
//
//
//        protected void onPostExecute(Boolean success) {
//
//        }
//    }

}


package com.pia.tchittchat.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.ApiManager2_0;
import com.pia.tchittchat.model.Auth;
import com.pia.tchittchat.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText login;
    private EditText mail;
    private EditText mail_confirmation;
    private EditText password;
    private EditText password_confirmation;
    private Button submitBtn;
    private ApiManager1_0 apiManager;
    private ApiManager2_0 apiManager2_0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mail = (EditText) findViewById(R.id.sign_up_mail);
        mail_confirmation = (EditText) findViewById(R.id.sign_up_mail_confirmation);
        password = (EditText) findViewById(R.id.sign_up_password);
        password_confirmation = (EditText) findViewById(R.id.sign_up_password_confirmation);
        login = (EditText) findViewById(R.id.sign_up_login);
        submitBtn = (Button) findViewById(R.id.submitButton);

        apiManager = ((MyApplication) getApplication()).getApiManager1_0();
        apiManager2_0 = ((MyApplication) getApplication()).getApiManager2_0();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegisterRequest();
            }
        });
    }


    private boolean checkEmail(){
        return mail.getText().toString().equals(mail_confirmation.getText().toString());
    }

    private boolean checkPassword(){
        // Check password length + carac ???
        return password.getText().toString().equals(password_confirmation.getText().toString());
    }


    private void sendRegisterRequest(){
        Auth authRequest = new Auth();
        authRequest.setLogin(login.getText().toString());
        authRequest.setPassword(password.getText().toString());
        if (checkEmail()){
            if(checkPassword()){
               // Call<Result> call = apiManager.registerUser(newUserLogin, newUserPassword);
                Call<Result> call = apiManager2_0.registerUser(authRequest);
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.body() != null) {
                            if (response.body().status == 200) {
                                Toast.makeText(SignUpActivity.this, "Hello", Toast.LENGTH_LONG).show();
                                Intent intentLogged = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intentLogged);
                            } else {
                                Toast.makeText(SignUpActivity.this, "User already exist.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            // If error.body
                            Toast.makeText(SignUpActivity.this, "User already exist.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            else{
                Toast.makeText(SignUpActivity.this,R.string.error_password_register,Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(SignUpActivity.this,R.string.error_mail,Toast.LENGTH_LONG).show();
        }
    }


}

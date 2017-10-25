package com.pia.tchittchat.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ConnectionManager;
import com.pia.tchittchat.rest.Result;

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
    private ConnectionManager connectionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();

        mail = (EditText) findViewById(R.id.sign_up_mail);
        mail_confirmation = (EditText) findViewById(R.id.sign_up_mail_confirmation);
        password = (EditText) findViewById(R.id.sign_up_password);
        password_confirmation = (EditText) findViewById(R.id.sign_up_password_confirmation);
        login = (EditText) findViewById(R.id.sign_up_login);
        submitBtn = (Button) findViewById(R.id.submitButton);

        connectionManager = ((MyApplication) getApplication()).getConnectionService();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegisterRequest();
            }
        });
    }


    private boolean checkEmail(){
        if(mail.getText().toString().equals(mail_confirmation.getText().toString()))
            return true;
        return false;
    }

    private boolean checkPassword(){
        // Check password length + carac ???
        if(password.getText().toString().equals(password_confirmation.getText().toString()))
            return true;
        return false;
    }


    private void sendRegisterRequest(){
        String newUserLogin = login.toString();
        String newUserPassword = password.toString();
        if (checkEmail()){
            if(checkPassword()){
                Call<Result> call = connectionManager.registerUser(login.getText().toString(), password.getText().toString());
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

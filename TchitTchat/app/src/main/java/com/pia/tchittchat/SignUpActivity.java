package com.pia.tchittchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private EditText login;
    private EditText mail;
    private EditText mail_confirmation;
    private EditText password;
    private EditText password_confirmation;
    private Button submitBtn;

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
                // Send Request
                // If reply is ok
                //      Return Home page
                // ELSE
                //      Stay on the page and display error message
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

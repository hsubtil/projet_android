package com.pia.tchittchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView date;
    TextView username;
    Button sendBtn;
    ProgressBar progressBar;
    EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (TextView) findViewById(R.id.username);
        date = (TextView) findViewById(R.id.date);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        message = (EditText) findViewById(R.id.message);

        username.setText(getIntent().getStringExtra("USERNAME"));


        DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDate = df.format(Calendar.getInstance().getTime());

        date.setText(currentDate);

        sendBtn.setOnClickListener ( new View.OnClickListener (){
            @Override
            public void onClick (View v){
                JSONObject msg = new JSONObject();
                Toast.makeText(MainActivity.this, message.getText().toString(),Toast.LENGTH_LONG ).show();
            }
        });
    }

}

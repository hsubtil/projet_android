package com.pia.tchittchat.model;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pia.tchittchat.MyAdapter;
import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ConnectionManager;
import com.pia.tchittchat.rest.MessageElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView date;
    TextView username;
    Button sendBtn;
    ProgressBar progressBar;
    EditText message;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (TextView) findViewById(R.id.username);
        date = (TextView) findViewById(R.id.date);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        message = (EditText) findViewById(R.id.message);
        password = getIntent().getStringExtra("PASSWORD");
        username.setText(getIntent().getStringExtra("USERNAME"));
        final ConnectionManager connectionManager = ((MyApplication) getApplication()).getConnectionService();
        final RecyclerView recyclerViewMessages = (RecyclerView) findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewMessages.setLayoutManager(mLayoutManager);


        DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDate = df.format(Calendar.getInstance().getTime());

        date.setText(currentDate);

        progressBar.setVisibility(View.VISIBLE);
        Call<List<MessageElement>> call = connectionManager.getMessages(username.getText().toString(), password.toString());
        call.enqueue(new Callback<List<MessageElement>>() {
            @Override
            public void onResponse(Call<List<MessageElement>> call, Response<List<MessageElement>> response) {
                    RecyclerView.Adapter myAdapter = new MyAdapter(response.body());
                    recyclerViewMessages.setAdapter(myAdapter);

                    Toast.makeText(MainActivity.this,"Messages retrieved",Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<MessageElement>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        sendBtn.setOnClickListener ( new View.OnClickListener (){
            @Override
            public void onClick (View v){
                {

                }
            }
        });
    }

}

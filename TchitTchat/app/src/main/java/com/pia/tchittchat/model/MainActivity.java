package com.pia.tchittchat.model;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.MessageElement;
import com.pia.tchittchat.rest.Result;
import com.pia.tchittchat.rest.ResultSendMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

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
    RecyclerView recyclerViewMessages;
    ApiManager1_0 apiManager;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (TextView) findViewById(R.id.username);
        date = (TextView) findViewById(R.id.date);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        message = (EditText) findViewById(R.id.message);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        password = getIntent().getStringExtra("PASSWORD");
        username.setText(getIntent().getStringExtra("USERNAME"));

        apiManager = ((MyApplication) getApplication()).getApiManager1_0();

        recyclerViewMessages = (RecyclerView) findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewMessages.setLayoutManager(mLayoutManager);


        /*DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDate = df.format(Calendar.getInstance().getTime());

        date.setText(currentDate);*/
        displayMessage();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                displayMessage();
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    sendMessage();
                }
            }
        });
    }


    private void displayMessage() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<MessageElement>> call = apiManager.getMessages(username.getText().toString(), password.toString());
        call.enqueue(new Callback<List<MessageElement>>() {
            @Override
            public void onResponse(Call<List<MessageElement>> call, Response<List<MessageElement>> response) {
                RecyclerView.Adapter myAdapter = new MyAdapter(response.body());
                recyclerViewMessages.setAdapter(myAdapter);

                Toast.makeText(MainActivity.this, "Messages retrieved", Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.INVISIBLE);

                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<MessageElement>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendMessage() {
        {
            MessageElement messageElement = new MessageElement();
            messageElement.setLogin(username.getText().toString());
            messageElement.setMessage(message.getText().toString());
            messageElement.setUuid(UUID.randomUUID().toString());

            /*JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("uuid", messageElement.getUuid());
            jsonObject.accumulate("login", messageElement.getLogin());
            jsonObject.accumulate("message", messageElement.getMessage());*/

                    Call<ResultSendMessage> call = apiManager.sendMessage(username.getText().toString(), password.toString(),messageElement);
                    call.enqueue(new Callback<ResultSendMessage>() {
                        @Override
                        public void onResponse(Call<ResultSendMessage> call, Response<ResultSendMessage> response) {
                            if (response.body() != null) {
                                if (response.body().status == 200) {
                                    Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
                                    message.setText("");
                                } else {
                                    Toast.makeText(MainActivity.this, "Something went wrong try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                @Override
                public void onFailure(Call<ResultSendMessage> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    }

}

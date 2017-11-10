package com.pia.tchittchat.view;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.pia.tchittchat.rest.MyAdapter;
import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.ApiManager2_0;
import com.pia.tchittchat.model.Image;
import com.pia.tchittchat.model.Messages;
import com.pia.tchittchat.model.ResultMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    TextView date;
    TextView username;
    Button sendBtn;
    EditText message;
    String login;
    String authToken;
    RecyclerView recyclerViewMessages;
    ApiManager1_0 apiManager;
    ApiManager2_0 apiManager2_0;

    SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = getSharedPreferences("authToken", 0);
        //username = (TextView) findViewById(R.id.username);
        //date = (TextView) findViewById(R.id.date);
        message = (EditText) findViewById(R.id.message);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);


        sendBtn = (Button) findViewById(R.id.sendBtn);
        Button profileBtn = (Button) findViewById(R.id.profileBtn);
        Button contactBtn = (Button) findViewById(R.id.contactBtn);

       // password = getIntent().getStringExtra("PASSWORD");
        login = getIntent().getStringExtra("USERNAME");
        authToken = mPrefs.getString("authToken", "null");

        apiManager = ((MyApplication) getApplication()).getApiManager1_0();
        apiManager2_0 = ((MyApplication) getApplication()).getApiManager2_0();


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

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogged = new Intent(MainActivity.this, ProfileActivity.class);
                intentLogged.putExtra("LOGIN", login);
                startActivity(intentLogged);
            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogged = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intentLogged);
            }
        });
    }

    private void displayMessage() {
        // Call<List<Messages>> call = apiManager.getMessages(username.getText().toString(), password.toString());
        Call<List<Messages>> call = apiManager2_0.getMessages(authToken, 20, 0);

        call.enqueue(new Callback<List<Messages>>() {
            @Override
            public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                RecyclerView.Adapter myAdapter = new MyAdapter(response.body());
                recyclerViewMessages.setAdapter(myAdapter);

                Toast.makeText(MainActivity.this, "Messages retrieved", Toast.LENGTH_LONG).show();

                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<Messages>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendMessage() {
        {
            Messages messages = new Messages();
            messages.setLogin(username.getText().toString());
            messages.setMessage(message.getText().toString());
            messages.setUuid(UUID.randomUUID().toString());
            ArrayList<Image> attachments = new ArrayList<Image>();
            messages.setAttachments(attachments);


            Call<ResultMessages> call = apiManager2_0.sendMessages(authToken, messages);

            call.enqueue(new Callback<ResultMessages>() {
                @Override
                public void onResponse(Call<ResultMessages> call, Response<ResultMessages> response) {
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
                public void onFailure(Call<ResultMessages> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    }

}

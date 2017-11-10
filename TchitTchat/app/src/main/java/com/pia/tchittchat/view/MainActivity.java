package com.pia.tchittchat.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pia.tchittchat.model.Attachment;
import com.pia.tchittchat.rest.MyAdapter;
import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.ApiManager2_0;
import com.pia.tchittchat.model.Image;
import com.pia.tchittchat.model.Messages;
import com.pia.tchittchat.model.ResultMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
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
    ApiManager2_0 apiManager2_0;


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
    }


    private void displayMessage() {
        progressBar.setVisibility(View.VISIBLE);
        // Call<List<Messages>> call = apiManager.getMessages(username.getText().toString(), password.toString());
        Call<List<Messages>> call = apiManager2_0.getMessages(Helper.createAuthToken(username.getText().toString(), password.toString()), 20, 0);

        call.enqueue(new Callback<List<Messages>>() {
            @Override
            public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                //List<Messages> m = response.body();
                //getMessageAttachments(m.get(2));
                RecyclerView.Adapter myAdapter = new MyAdapter(response.body(), MainActivity.this);
                recyclerViewMessages.setAdapter(myAdapter);

                Toast.makeText(MainActivity.this, "Messages retrieved", Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.INVISIBLE);

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
            //String [] attachments;
            //messages.setImage(attachments);


            Call<ResultMessages> call = apiManager2_0.sendMessages(Helper.createAuthToken(username.getText().toString(), password.toString()), messages);

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

    public void getMessageAttachments(Messages message, final ImageView attachement) {
        String [] images = message.getImage();
        String fileName = "";
        String [] imagesNames;
        progressBar.setVisibility(View.VISIBLE);
        if(images!= null){
            imagesNames = new String[images.length];
            if(images.length != 0){
                for (int i = 0; i<images.length;i++){
                    String [] filePath = images[i].split("/");
                    fileName = filePath [filePath.length-1];
                    imagesNames [i] = fileName;
                }
            }



        String uuid = "msg-"+message.getUuid();
        Call<ResponseBody> call = apiManager2_0.getAttachements(Helper.createAuthToken(username.getText().toString(), password.toString()),uuid, imagesNames[0]);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Glide.with(MainActivity.this)
                            .load(response.body().bytes())
                            .asBitmap()
                            .placeholder(R.layout.message)
                            .into(attachement);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }



            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
        }


    }

}

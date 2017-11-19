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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pia.tchittchat.rest.MyAdapter;
import com.pia.tchittchat.MyApplication;
import com.pia.tchittchat.R;
import com.pia.tchittchat.rest.ApiManager1_0;
import com.pia.tchittchat.rest.ApiManager2_0;
import com.pia.tchittchat.model.Messages;
import com.pia.tchittchat.model.ResultMessages;
import com.pia.tchittchat.tools.NetworkCom;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.socket.emitter.Emitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private  NetworkCom socket;
    private TextView date;
    private Button sendBtn;
    private EditText message;
    private String login;
    private String authToken;
    private RecyclerView recyclerViewMessages;
    private ScrollView Form;
    private ApiManager1_0 apiManager;
    private ApiManager2_0 apiManager2_0;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = getSharedPreferences("authToken", 0);
        message = (EditText) findViewById(R.id.message);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        Form = (ScrollView) findViewById(R.id.Form);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        Button profileBtn = (Button) findViewById(R.id.profileBtn);
        Button contactBtn = (Button) findViewById(R.id.contactBtn);

        authToken = mPrefs.getString("authToken", "null");

        apiManager = ((MyApplication) getApplication()).getApiManager1_0();
        apiManager2_0 = ((MyApplication) getApplication()).getApiManager2_0();


        recyclerViewMessages = (RecyclerView) findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewMessages.setLayoutManager(mLayoutManager);
        /********SOCKET INIT********/
        socket = new NetworkCom();
        socket.getmSocket().on("inbound_msg",onNewMessage);
        socket.getmSocket().on("post_success_msg",postSuccess);
        socket.getmSocket().on("bad_request_msg",badRequest);
        socket.getmSocket().on("user_typing_inbound_msg",typingInbound);

        displayMessage();
        Form.scrollTo(0, Form.getBottom());


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                displayMessage();
                Form.scrollTo(0, Form.getBottom());

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
        Call<List<Messages>> call = apiManager2_0.getMessages(authToken, 20, 0);

        call.enqueue(new Callback<List<Messages>>() {
            @Override
            public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {

                RecyclerView.Adapter myAdapter = new MyAdapter(response.body(), MainActivity.this);
                recyclerViewMessages.setAdapter(myAdapter);

                Toast.makeText(MainActivity.this, "New message !", Toast.LENGTH_LONG).show();

                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<Messages>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendMessage() {
        String login =  mPrefs.getString("authLogin", "null");
        String sessionToken =  mPrefs.getString("sessionToken", "null");
        // Socket test. -> Not working
  //      socket.emitUserTyping(login,sessionToken);
  //      socket.emitMessage(login,sessionToken,UUID.randomUUID().toString(),message.getText().toString());

            Messages messages = new Messages();
            messages.setLogin(login);
            messages.setMessage(message.getText().toString());
            messages.setUuid(UUID.randomUUID().toString());
            //messages.setImage(attachments);

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

    public void getMessageAttachments(Messages message, final ImageView attachement) {
        String [] images = message.getImage();
        String fileName = "";


        if(images!= null){
            if(images.length != 0){
                for (int i = 0; i<images.length;i++){
                    String [] filePath = images[i].split("/");
                    fileName = filePath [filePath.length-1];

                    String uuid = "msg-"+message.getUuid();
                    Call<ResponseBody> call = apiManager2_0.getAttachements(authToken,uuid, fileName);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                Glide.with(MainActivity.this)
                                        .load(response.body().bytes())
                                        .asBitmap()
                                        .placeholder(R.drawable.border)
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






    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            displayMessage();
            return;
        }
    };

    private Emitter.Listener postSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obk = (JSONObject) args[0];

            String uuid = "";
            try {
                uuid = obk.getString("uuid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
    };
    private Emitter.Listener badRequest = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obk = (JSONObject) args[0];


            return;
        }
    };

    private Emitter.Listener typingInbound = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obk = (JSONObject) args[0];
            String user = "";
            try {
                user = obk.getString("login");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.destroySocket();
    }
}

package com.project.studentapp;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editText;
    ImageView imageView;
    ArrayList<Chatsmodal> chatsModalArrayList;
    ChatAdapter chatAdapter;
    private  final String USER_KEY = "user";
    private  final String BOT_KEY = "bot";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chat_recycler);
        editText = findViewById(R.id.edt_msg);
        imageView = findViewById(R.id.send_btn);
        chatsModalArrayList = new ArrayList<>();
        chatsModalArrayList.add(new Chatsmodal("Hello Student How may I Help You!",BOT_KEY));
        chatAdapter = new ChatAdapter(chatsModalArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatAdapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(ChatActivity.this,"Please enter your message",Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(editText.getText().toString());
                editText.setText("");
            }
        });



    }

    @SuppressLint("NotifyDataSetChanged")
    private void getResponse(String message) {
        chatsModalArrayList.add(new Chatsmodal(message,USER_KEY));
        chatAdapter.notifyDataSetChanged();
        String url = "https://stu-chatbot.herokuapp.com/"+message;
        String BASE_URL = "https://stu-chatbot.herokuapp.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroFitApi retroFitApi = retrofit.create(RetroFitApi.class);
        Call<MsgModal> call = retroFitApi.getMessage(url);
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if(response.isSuccessful()){
                    MsgModal msgModal = response.body();
                    chatsModalArrayList.add(new Chatsmodal(msgModal.getMessage(),BOT_KEY));
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatsModalArrayList.size()-1);
                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                chatsModalArrayList.add(new Chatsmodal("no response",BOT_KEY));
                chatAdapter.notifyDataSetChanged();
            }
        });
    }
}
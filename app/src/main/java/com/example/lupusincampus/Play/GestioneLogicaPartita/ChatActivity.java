package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

import java.util.ArrayList;

public class ChatActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<String> messageList;
    private EditText editTextMessage;
    private Button buttonSend;
    //    private WebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_testuale);

        SharedActivity sharedActivity = SharedActivity.getInstance(getApplicationContext());
        String nickname = sharedActivity.getNickname();
        recyclerView = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, nickname);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        //connessione ala socket
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                if (!message.isEmpty()) {
                    //socket
                    messageList.add(nickname +": " + message);
                    messageAdapter.notifyDataSetChanged();
                    editTextMessage.setText("");
                }
            }
        });
    }


}



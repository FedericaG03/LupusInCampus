package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.example.lupusincampus.API.websocket.Subscriber;
import com.example.lupusincampus.API.websocket.WebSocketObserver;
import com.example.lupusincampus.API.websocket.StompClientManager;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.ScatteringByteChannel;

public class ChatActivity extends BaseActivity implements Subscriber {

    private static final String TAG = "ChatActivity";
    private EditText messageInput;
    private Button sendMessageButton;
    private LinearLayout messageContainer;
    private StompClientManager stompClientManager;
    private String lobbyCode ="155968";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_testuale);

        WebSocketObserver.getInstance().subscribe(WebSocketObserver.EventType.CHAT_MESSAGE, this);

        SharedActivity sharedActivity = SharedActivity.getInstance(getApplicationContext());
        String nickname = sharedActivity.getNickname();
        messageInput = findViewById(R.id.messageInput);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        messageContainer = findViewById(R.id.messageContainer);

        // Inizializza StompClientManager
        stompClientManager = StompClientManager.getInstance(this);


        // Sottoscrizione ai messaggi quando il WebSocket è connesso
        stompClientManager.connect("lobbyCode");  // Usa il codice della lobby come parametro

        // Gestire l'invio del messaggio
        sendMessageButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString();
            if (!message.isEmpty()) {
                stompClientManager.sendChatMessage("PlayerName", message);  // Passa il nome del giocatore e il messaggio
                messageInput.setText("");  // Svuota il campo di input
            }
        });
    }

    // Aggiungere un messaggio alla UI
    private void addMessageToChat(String sender, String message) {
        TextView messageTextView = new TextView(this);
        messageTextView.setText(sender + ": " + message);
        messageContainer.addView(messageTextView);

        // Scorrimento automatico
        messageContainer.post(() -> messageContainer.scrollTo(0, messageContainer.getHeight()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stompClientManager.disconnect();  // Disconnessione quando l'attività è distrutta
        WebSocketObserver.getInstance().unsubscribe(WebSocketObserver.EventType.CHAT_MESSAGE, this);
    }

    @Override
    public void update(JSONObject data) {
        try {
            String sender = data.getString("sender");
            String message = data.getString("message");
            addMessageToChat(sender, message);
        }catch (JSONException ex){
            Log.e(TAG, "update: ", ex);
        }
    }
}





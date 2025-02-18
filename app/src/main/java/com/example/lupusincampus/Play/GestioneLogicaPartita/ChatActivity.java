package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
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

public class ChatActivity extends BaseActivity implements Subscriber {

    private static final String TAG = "ChatActivity";
    private EditText messageInput;
    private Button sendMessageButton;
    private LinearLayout messageContainer;
    private StompClientManager stompClientManager;
     private static final int RETURN_DELAY = 180000; // 3 minuti in millisecondi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_testuale);

        WebSocketObserver.getInstance().subscribe(WebSocketObserver.EventType.CHAT_MESSAGE, this);

        Intent intent = getIntent();

        SharedActivity sharedActivity = SharedActivity.getInstance(getApplicationContext());
        String nickname = sharedActivity.getNickname();
        messageInput = findViewById(R.id.messageInput);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        messageContainer = findViewById(R.id.messageContainer);

        // Inizializza StompClientManager
        stompClientManager = StompClientManager.getInstance(this);

        // Gestire l'invio del messaggio
        sendMessageButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString();
            if (!message.isEmpty()) {

                stompClientManager.sendChatMessage(nickname, message);  // Passa il nome del giocatore e il messaggio
                messageInput.setText("");  // Svuota il campo di input
            }
        });

        // Dopo 3 minuti, torna automaticamente a StoryActivity
        // Chiude l'activity dopo il tempo
        new Handler().postDelayed(this::finish, RETURN_DELAY);
    }

    // Aggiungere un messaggio alla UI
    private void addMessageToChat(String sender, String message) {
        TextView messageTextView = new TextView(this);
        messageTextView.setText(sender + ": " + message);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        messageContainer.addView(messageTextView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stompClientManager.disconnect();  // Disconnessione quando l'attività è distrutta
        WebSocketObserver.getInstance().unsubscribe(WebSocketObserver.EventType.CHAT_MESSAGE, this);
    }

    @Override
    public void update(JSONObject data, WebSocketObserver.EventType eventType) {
        try {
            String sender = data.getString("sender");
            String message = data.getString("message");
            addMessageToChat(sender, message);
        }catch (JSONException ex){
            Log.e(TAG, "update: ", ex);
        }
    }
}





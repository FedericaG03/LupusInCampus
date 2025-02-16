package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.lupusincampus.API.websocket.StompClientManager;
import com.example.lupusincampus.API.websocket.Subscriber;
import com.example.lupusincampus.API.websocket.WebSocketObserver;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.FileUtils;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class PartitaActivity extends BaseActivity implements Subscriber {
    private static final String TAG = "PartitaActivity";
    private static final String FILE_NAME = "Il Campus Maledetto.txt";
    private Button btnExit, btnPlayers;
    private TextView storyText, playerRole;
    private ImageView playerAvatar;
    private SharedActivity sharedActivity;

    //Gestione lettura file
    private static final int DELAY_TIME = 180000; // 3 minuti in millisecondi
    private FileUtils fileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partita_activity);

        sharedActivity = SharedActivity.getInstance(this);

        Intent intent = getIntent();
        String lobbyCode = intent.getStringExtra("lobbyCode");
        String role = intent.getStringExtra("role");


        String nickname = sharedActivity.getNickname();
        StompClientManager.getInstance(this).sendAck(nickname);
        TextView profileButton = findViewById(R.id.profile_btn);
        profileButton.setText(nickname);

        btnExit = findViewById(R.id.btn_exit);
        btnPlayers = findViewById(R.id.btn_players);
        storyText = findViewById(R.id.story_text);


        playerRole = findViewById(R.id.player_role);
        playerRole.setText(role);

        //setta immagine in base al ruolo
        playerAvatar = findViewById(R.id.player_avatar);
        String fileName = "logo_" + role.toLowerCase().replaceAll(" ", "_") + ".png";
        Log.d(TAG, "onCreate: filename is:" + fileName);
        int resId = getResources().getIdentifier(fileName, "drawable", getPackageName());


        if (resId != 0) { // Controlla se l'immagine esiste
            playerAvatar.setImageResource(resId);
        } else {
            // Immagine non trovata, imposta un'immagine di default
            playerAvatar.setImageResource(R.drawable.logo);
        }


        // Leggi la prima riga e mostra nella TextView
        fileUtils = new FileUtils(this, FILE_NAME);

        // Mostra la prossima riga del file
        storyText.setText(fileUtils.getNextLine());

        // Dopo 3 minuti, passa automaticamente a ChatActivity
        new Handler().postDelayed(() ->{
            Intent intent_chat = new Intent(this, ChatActivity.class);
            intent_chat.putExtra("lobbyCode", lobbyCode);
            startActivity(intent_chat);
        },DELAY_TIME);

        // Richiesta del ruolo dal server
       // fetchRoleFromServer();

        // Listener per uscire dalla partita
        btnExit.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });
        // Listener per mostrare i giocatori
        btnPlayers.setOnClickListener(v -> Log.d(TAG, "Mostra lista giocatori"));
    }
    @Override
    public void update(JSONObject data, WebSocketObserver.EventType eventType) {

        if (eventType == WebSocketObserver.EventType.ROLE){
            try {
                Log.d(TAG, "update: ricevuto ruolo " + data.getString("data"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }
}



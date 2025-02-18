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


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.lupusincampus.API.websocket.StompClientManager;
import com.example.lupusincampus.API.websocket.Subscriber;
import com.example.lupusincampus.API.websocket.WebSocketObserver;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.FileUtils;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;


public class PartitaActivity extends BaseActivity implements Subscriber {
    private static final String TAG = "PartitaActivity";
    private static final String FILE_NAME = "Il Campus Maledetto.txt";
    private Button btnExit, btnPlayers;
    private TextView storyText, playerRole;
    private ImageView playerAvatar;
    private SharedActivity sharedActivity;
    private String lobbyCode;

    //Gestione lettura file
    private static final int DELAY_TIME = 180000; // 3 minuti in millisecondi
    private FileUtils fileUtils;


    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partita_activity);

        sharedActivity = SharedActivity.getInstance(this);

        Intent intent = getIntent();
        lobbyCode = intent.getStringExtra("lobbyCode");
        String role = intent.getStringExtra("role");

        String nickname = sharedActivity.getNickname();
        StompClientManager.getInstance(this).sendAck(nickname);

        WebSocketObserver.getInstance().subscribe(WebSocketObserver.EventType.NEXT_PHASE, this);

        TextView profileButton = findViewById(R.id.profile_btn);
        profileButton.setText(nickname);

        btnExit = findViewById(R.id.btn_exit);
        btnPlayers = findViewById(R.id.btn_players);
        storyText = findViewById(R.id.story_text);

        playerRole = findViewById(R.id.player_role);
        playerRole.setText(role);

        //setta immagine in base al ruolo
        playerAvatar = findViewById(R.id.player_avatar);

        // Pulizia del nome ruolo per generare il nome file corretto
        String fileName = "logo_" + role.toLowerCase().trim().replaceAll(" ", "_");
        Log.d(TAG, "onCreate: Generated filename is: " + fileName);

        // Ottiene l'ID della risorsa drawable
        int resId = getResources().getIdentifier(fileName, "drawable", getPackageName());
        Log.d(TAG, "onCreate: Resource ID found: " + resId);

        // Controlla se l'immagine esiste, altrimenti imposta un'immagine di default
        if (resId != 0) {
            playerAvatar.setImageResource(resId);
        } else {
            Log.w(TAG, "onCreate: Image not found, setting default image.");
            playerAvatar.setImageResource(R.drawable.logo); // Immagine di default
        }

        // Dopo 3 minuti, passa automaticamente a ChatActivity
        new Handler().postDelayed(() ->{
            Intent intent_chat = new Intent(this, ChatActivity.class);
            intent_chat.putExtra("lobbyCode", lobbyCode);
            startActivity(intent_chat);
        },DELAY_TIME);

        // Listener per uscire dalla partita
        btnExit.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });
        // Listener per mostrare i giocatori
        btnPlayers.setOnClickListener(v -> Log.d(TAG, "Mostra lista giocatori"));

        this.getIntent().putExtra("game_phase", "werewolves");
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String game_phase = this.getIntent().getStringExtra("game_phase");
        if (game_phase == null){
            game_phase = "werewolves";
        }
        // Leggi la prima riga e mostra nella TextView
        fileUtils = new FileUtils(this, FILE_NAME);
        // Mostra la prossima riga del file
        storyText.setText(fileUtils.getNextLine());

        switch (game_phase){
            case "werewolves": {
                if (playerRole.getText().equals("Studenti fuori corso")){
                    //can choose the player to kill

                    activityResultLauncher = registerForActivityResult(
                            new ActivityResultContracts.StartActivityForResult(),
                            result -> {
                                if (result.getResultCode() == PartitaActivity.RESULT_OK) {
                                    Intent data = result.getData();

                                    String phase = data.getStringExtra("game_phase");
                                    if (phase == null){
                                        phase = "werewolves";
                                    }
                                    String voted_player = data.getStringExtra("voted_player");

                                    StompClientManager.getInstance(this).sendVotedPlayerOnPhase(voted_player, phase);
                                }
                            }
                    );


                }
                break;
            }
            case "bodyguard": {
                if (playerRole.getText().equals("Rettore")){
                    //can choose the player to protect
                }
                break;
            }
            case "seer": {
                if (playerRole.getText().equals(("Ricercatore"))){
                    //can choos the player to check
                }
                break;
            }
            case "discussion": {
                //must vote for a player or skip
            }
        }


    }

    @Override
    public void update(JSONObject data, WebSocketObserver.EventType eventType) {
        if (eventType == WebSocketObserver.EventType.NEXT_PHASE){
            try {
               this.getIntent().putExtra("GAME_PHASE", data.getString("data"));
               onResume();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }
}



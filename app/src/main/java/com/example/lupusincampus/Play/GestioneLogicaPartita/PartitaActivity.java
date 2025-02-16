package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.API.websocket.Subscriber;
import com.example.lupusincampus.API.websocket.WebSocketObserver;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.FileUtils;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

import java.util.ArrayList;
import java.util.List;

public class PartitaActivity extends BaseActivity implements Subscriber {
    private static final String TAG = "PartitaActivity";
    private static final String FILE_NAME = "Il Campus Maledetto.txt";
    private Button btnExit, btnPlayers;
    private TextView storyText, playerName, roleDescription;
    private ImageView playerAvatar;
    private SharedActivity sharedActivity;

    //Gestione lettura file
    private static final int DELAY_TIME = 180000; // 3 minuti in millisecondi
    private FileUtils fileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partita_activity);

        WebSocketObserver.getInstance().subscribe(WebSocketObserver.EventType.ROLE, this);

        sharedActivity = SharedActivity.getInstance(this);

        Intent intent = getIntent();
        int lobbyCode = intent.getIntExtra("lobbyCode",0);


        String nickname = sharedActivity.getNickname();
        TextView profileButton = findViewById(R.id.profile_btn);
        profileButton.setText(nickname);

        btnExit = findViewById(R.id.btn_exit);
        btnPlayers = findViewById(R.id.btn_players);

        playerAvatar = findViewById(R.id.player_avatar);
        storyText = findViewById(R.id.story_text);
        playerName = findViewById(R.id.player_name);


        // Leggi la prima riga e mostra nella TextView
        fileUtils = new FileUtils(this, FILE_NAME);

        // Mostra la prossima riga del file
        storyText.setText(fileUtils.getNextLine());

        // Dopo 3 minuti, passa automaticamente a ChatActivity
        new Handler().postDelayed(() ->{
            Intent intent_chat = new Intent(this, ChatActivity.class);
            intent_chat.getIntExtra("lobbyCode", lobbyCode);
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

    // Metodo per ottenere il ruolo dal server
 /*   private void fetchRoleFromServer() {
        serverConnector.fetchRole(this,new ServerConnector.FetchDataCallback() {
            @Override
            public void onSuccess(String jsonResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONObject roleObject = jsonObject.getJSONObject("role");  // Estrai l'oggetto "role"

                    // Estrai i dettagli del ruolo
                    String roleName = roleObject.getString("name");
                    String roleDescriptionText = roleObject.getString("description");
                    int roleImageResId = getResources().getIdentifier(
                            roleObject.getString("imageResId"), "drawable", getPackageName());

                    // Crea un oggetto Ruolo
                    Ruolo ruolo = new Ruolo(roleName, roleDescriptionText, roleImageResId);
                    playerAvatar.setImageResource(ruolo.getImageResId());  // Imposta l'immagine dell'avatar
                    playerName.setText(ruolo.getNome());  // Imposta il nome del ruolo
                    roleDescription.setText(ruolo.getDescrizione());  // Imposta la descrizione del ruolo

                } catch (JSONException e) {
                    Log.e(TAG, "Errore nell'analisi del ruolo: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Errore nel recupero del ruolo: " + e.getMessage(), e);
            }

            @Override
            public void onServerOffline(Exception e) {
                Log.e(TAG, "Errore connessione server: " + e.getMessage(), e);
            }

        });
    }*/

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



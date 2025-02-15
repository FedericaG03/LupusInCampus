package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lupusincampus.API.LobbyAPI;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;


public class CreateLobbyActivity extends BaseActivity {

    private static final String TAG = "CreateLobbyActivity";
    private TextView tvCreateLobby;
    private TextView rbPrivate;
    private TextView rbPublic;
    private Button btnCreateLobby;
    private View backBtn;
    private String lobbyType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_lobby_activity);
        LobbyAPI lobbyAPI = new LobbyAPI();

        tvCreateLobby = findViewById(R.id.tvCreateLobby);
        rbPrivate = findViewById(R.id.rbPrivate);
        rbPublic = findViewById(R.id.rbPublic);
        backBtn = findViewById(R.id.back_btn);

        rbPublic.setOnClickListener(view -> {
            lobbyType = "Pubblica";
            lobbyAPI.doCreateLobby(getApplicationContext(), lobbyType);
            Log.d(TAG, "onClick: invio richiesta creazione lobby tipo: " + lobbyType);

            // Crea l'Intent per aprire LobbyActivityWait e passa il tipo di lobby
            Intent intent = new Intent(getApplicationContext(), LobbyActivityWait.class);
            Log.d(TAG, "onClick: passo alla prossima pagina e passo il tipo di lobby: " + intent.toString());
            intent.putExtra("lobbyType", lobbyType);  // Passa il tipo di lobby
            startActivity(intent);
        });

        rbPrivate.setOnClickListener(view -> {
            lobbyType = "Privata" ;
            lobbyAPI.doCreateLobby(getApplicationContext(), lobbyType);
            Log.d(TAG, "onClick: invio richiesta creazione lobby tipo: " + lobbyType);

            // Crea l'Intent per aprire LobbyActivityWait e passa il tipo di lobby
            Intent intent = new Intent(getApplicationContext(), LobbyActivityWait.class);
            Log.d(TAG, "onClick: passo alla prossima pagina e passo il tipo di lobby: " + intent.toString());
            intent.putExtra("lobbyType", lobbyType);  // Passa il tipo di lobby
            startActivity(intent);
        });


        // Imposta un listener per il pulsante "Indietro" (o esci)
        backBtn.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

    }


}

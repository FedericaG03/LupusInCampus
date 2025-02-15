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
        });

        rbPrivate.setOnClickListener(view -> {
            lobbyType = "Privata" ;
            lobbyAPI.doCreateLobby(getApplicationContext(), lobbyType);
            Log.d(TAG, "onClick: invio richiesta creazione lobby tipo: " + lobbyType);
        });


        // Imposta un listener per il pulsante "Indietro" (o esci)
        backBtn.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

    }


}

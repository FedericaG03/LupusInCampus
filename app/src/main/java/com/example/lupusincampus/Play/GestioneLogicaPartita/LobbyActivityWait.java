package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.API.LobbyAPI;
import com.example.lupusincampus.R;

public class LobbyActivityWait extends AppCompatActivity {

    private TextView numberPlayer;
    private RecyclerView recyclerFriends;
    private Button btnStartGame;
    private static final String TAG = "LobbyActivityWait";
    private boolean isRunning = true;

    private LobbyAPI lobbyAPI; // Aggiungi un'istanza di LobbyAPI
    private LobbyDatabaseHelper dbHelper; // Istanza per il database delle lobby

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_waiting);

        numberPlayer = findViewById(R.id.number_player);
        recyclerFriends = findViewById(R.id.recycler_friends);
        btnStartGame = findViewById(R.id.btn_start_game);

        // Crea un'istanza di LobbyDatabaseHelper
        dbHelper = new LobbyDatabaseHelper(this);
        int lastCode = dbHelper.getLastRow();

        // Imposta RecyclerView per la lista degli amici
        recyclerFriends.setLayoutManager(new LinearLayoutManager(this));

        // Crea un'istanza di LobbyAPI
        lobbyAPI = new LobbyAPI();

        // Imposta un listener per il bottone di uscita
        Button btnExit = findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

        // Funzione per aggiornare la UI con il numero di giocatori
        updateNumberPlayerInUI(dbHelper.getNumPlayer(lastCode));

        // Inizializza il processo di join nella lobby
        joinLobby(lastCode);
    }

    /**
     * Metodo per aggiornare il numero di giocatori sulla UI.
     * @param numPlayer Numero di giocatori da visualizzare.
     */
    private void updateNumberPlayerInUI(int numPlayer) {
        runOnUiThread(() -> numberPlayer.setText(String.valueOf(numPlayer)));
    }

    /**
     * Metodo per entrare in una lobby e aggiornare i dati.
     */
    private void joinLobby(int lastCode) {
        Log.d(TAG, "joinLobby: Richiesta al server per unire il giocatore alla lobby");

        // Supponiamo che il codice della lobby sia già disponibile (può essere passato tramite Intent)
        // Eseguiamo la richiesta di join nella lobby
        lobbyAPI.doJoinLobby(this, lastCode); // Chiamata alla funzione in LobbyAPI
    }


}

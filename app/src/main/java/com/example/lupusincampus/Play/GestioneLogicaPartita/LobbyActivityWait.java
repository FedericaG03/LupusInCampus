package com.example.lupusincampus.Play.GestioneLogicaPartita;

import com.example.lupusincampus.API.FriendAPI;
import com.example.lupusincampus.Amici.ListaAmiciAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.API.LobbyAPI;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivityWait extends BaseActivity {

    private TextView numberPlayer;
    private RecyclerView recyclerFriends;
    private Button btnStartGame;
    private ListView friends_waiting;
    private static final String TAG = "LobbyActivityWait";

    private LobbyAPI lobbyAPI = new LobbyAPI();; // Aggiungi un'istanza di LobbyAPI
    private LobbyDatabaseHelper dbHelper; // Istanza per il database delle lobby

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_waiting);

        numberPlayer = findViewById(R.id.number_player);
        recyclerFriends = findViewById(R.id.recycler_friends);
        btnStartGame = findViewById(R.id.btn_start_game);
        friends_waiting = findViewById(R.id.friends_waiting);

        // Crea un'istanza di LobbyDatabaseHelper
        dbHelper = new LobbyDatabaseHelper(this);

        int lastCode = dbHelper.getLastRow();
        Log.d(TAG, "onCreate: code lobby" + lastCode);
        // Imposta RecyclerView per la lista degli amici
        Log.d(TAG, "onCreate: code lobby"+lastCode);
        
        // Funzione per aggiornare la UI con il numero di giocatori
        updateNumberPlayerInUI(dbHelper.getNumPlayer(lastCode));


        // Imposta l'adapter per la RecyclerView con la lista amici
        List<Player> friendList = SharedActivity.getInstance(getApplicationContext()).getFriendList();

        // Configura il RecyclerView
        ListaAmiciAdapter listaAmiciAdapter = new ListaAmiciAdapter(friendList);
        recyclerFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerFriends.setAdapter(listaAmiciAdapter);


        List<String> player_in_waiting = new ArrayList<>();// Prendi i dati dal DB
        player_in_waiting = dbHelper.getPlayesByLobbyID(lastCode);

        //Giocatori appena entro in lobby
        ArrayAdapter<String> nicknameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, player_in_waiting);
        friends_waiting.setAdapter(nicknameAdapter);

        //
        String lobbyType = dbHelper.getLobbyType(lastCode);

        // Imposta un listener per il bottone di uscita
        Button btnExit = findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(v -> {
            lobbyAPI.doLeaveLobby(this, lastCode);
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });


        btnStartGame.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),"Iniziamo a giocare!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), PartitaActivity.class);
            Log.d(TAG, "onClick: vado alla partita, iniziamo a giocare: " + intent.toString());
            intent.putExtra("lobbyType", lobbyType);  // Passa il tipo di lobby
            startActivity(intent);

        });
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

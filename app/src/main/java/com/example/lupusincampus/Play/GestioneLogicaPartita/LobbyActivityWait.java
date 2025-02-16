package com.example.lupusincampus.Play.GestioneLogicaPartita;

import com.example.lupusincampus.API.FriendAPI;
import com.example.lupusincampus.API.websocket.StompClientManager;
import com.example.lupusincampus.API.websocket.Subscriber;
import com.example.lupusincampus.API.websocket.WebSocketObserver;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivityWait extends BaseActivity implements Subscriber {

    private TextView numberPlayer;
    private RecyclerView recyclerFriends;
    private Button btnStartGame;
    private ListView friends_waiting;
    private static final String TAG = "LobbyActivityWait";
    List<String> player_in_waiting = new ArrayList<>();
    ArrayAdapter<String> nicknameAdapter;

    private LobbyAPI lobbyAPI = new LobbyAPI();
    private LobbyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_waiting);

        WebSocketObserver.getInstance().subscribe(WebSocketObserver.EventType.PLAYER_JOINED, this);
        WebSocketObserver.getInstance().subscribe(WebSocketObserver.EventType.PLAYER_LEFT, this);


        numberPlayer = findViewById(R.id.number_player);
        recyclerFriends = findViewById(R.id.recycler_friends);
        btnStartGame = findViewById(R.id.btn_start_game);
        friends_waiting = findViewById(R.id.friends_waiting);

        dbHelper = new LobbyDatabaseHelper(this);

        int lastCode = dbHelper.getLastRow();
        Log.d(TAG, "onCreate: code lobby" + lastCode);


        // Funzione per aggiornare la UI con il numero di giocatori
        updateNumberPlayerInUI(dbHelper.getNumPlayer(lastCode));

        // Imposta l'adapter per la RecyclerView con la lista amici
        List<Player> friendList = SharedActivity.getInstance(getApplicationContext()).getFriendList();

        // Configura il RecyclerView
        ListaAmiciAdapter listaAmiciAdapter = new ListaAmiciAdapter(friendList);
        recyclerFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerFriends.setAdapter(listaAmiciAdapter);


        player_in_waiting = dbHelper.getPlayesByLobbyID(lastCode);
        Log.d(TAG, "onCreate: giocatori nella lobby " + player_in_waiting.size());
         // Prendi i dati dal DB
        // Creazione dell'ArrayAdapter
        nicknameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, player_in_waiting);
        friends_waiting.setAdapter(nicknameAdapter);


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

        //nicknameAdapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StompClientManager.getInstance(this).disconnect();
    }

    @Override
    public void update(JSONObject data, WebSocketObserver.EventType eventType) {

        try {
            String player = data.getString("player");
            String lobbyCode = data.getString("lobbyCode");

            if (player.equals(SharedActivity.getInstance(this).getNickname())){
                return;
            }

            switch (eventType){
                case PLAYER_JOINED: {
                    player_in_waiting.add(player);
                    nicknameAdapter.notifyDataSetChanged();

                    dbHelper. insertPlayersIntoLobby(Integer.parseInt(lobbyCode), List.of(player));
                    break;
                }
                case PLAYER_LEFT: {
                    player_in_waiting.remove(player);
                    //nicknameAdapter.notifyDataSetChanged();
                    dbHelper.removePlayersFromLobby(Integer.parseInt(lobbyCode), List.of(player));

                    refreshPlayerList(Integer.parseInt(lobbyCode));

                    break;
                }
            }


        }catch (JSONException ex){
            Log.e(TAG, "update: ", ex);
        }
    }


    /**
     * Metodo per aggiornare la lista dei giocatori dalla base di dati e aggiornare la UI.
     */
    private void refreshPlayerList(int lobbyCode) {
        runOnUiThread(() -> {
            // Recupera i giocatori aggiornati dal database
            player_in_waiting.clear();
            player_in_waiting.addAll(dbHelper.getPlayesByLobbyID(lobbyCode));

            // Notifica l'adapter dell'aggiornamento
            nicknameAdapter.notifyDataSetChanged();

            // Aggiorna il numero di giocatori nella UI
            updateNumberPlayerInUI(player_in_waiting.size());
        });
    }
}

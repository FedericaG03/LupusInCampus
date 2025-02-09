package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;

public class LobbyActivityWait extends AppCompatActivity {

    private TextView numberPlayer;
    private RecyclerView recyclerFriends;
    private ServerConnector serverConnector;
    private static final String TAG = "LobbyActivity";
    private boolean isRunning = true;
    private Button btnStartGame;
    private int currentPlayers = 0;
    private int maxPlayers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_waiting);

        numberPlayer = findViewById(R.id.number_player);
        recyclerFriends = findViewById(R.id.recycler_friends);
        Button btnAmici = findViewById(R.id.btn_amici);
        Button btnExit = findViewById(R.id.btn_exit);
        btnStartGame = findViewById(R.id.btn_start_game);
        serverConnector = new ServerConnector();

        //TODO: Configura RecyclerView per lista amici
        recyclerFriends.setLayoutManager(new LinearLayoutManager(this));

//        btnAmici.setOnClickListener(v -> toggleFriendList());
        btnExit.setOnClickListener(v -> {
            isRunning = false;
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

       // btnStartGame.setOnClickListener(v -> startGame());

        // Avvia polling per numero giocatori
        //startLobbyPolling();
    }

    /*private void startLobbyPolling() {
    new Thread(() -> {
            while (isRunning) {
                serverConnector.("/controller/game/lobby_status", new ServerConnector.FetchDataCallback() {
                    @Override
                    public void onSuccess(String jsonResponse) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            currentPlayers = jsonObject.getInt("currentPlayers");
                            maxPlayers = jsonObject.getInt("maxPlayers");

                            runOnUiThread(() -> {
                                numberPlayer.setText(currentPlayers + "/" + maxPlayers);

                                // Se il numero massimo è raggiunto, avvia automaticamente la partita
                                if (currentPlayers == maxPlayers) {
                                    startGame();
                                }

                                // Mostra il pulsante se ci sono almeno 2 giocatori
                                btnStartGame.setVisibility(currentPlayers >= 2 ? View.VISIBLE : View.GONE);
                            });

                        } catch (JSONException e) {
                            Log.e(TAG, "Errore parsing JSON: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Errore aggiornamento lobby: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(LobbyActivity.this, "Errore connessione", Toast.LENGTH_SHORT).show());
                    }
                });

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Thread interrotto: " + e.getMessage());
                }
            }
        }).start();
    }

    private void toggleFriendList() {
        if (recyclerFriends.getVisibility() == View.VISIBLE) {
            recyclerFriends.setVisibility(View.GONE);
        } else {
            recyclerFriends.setVisibility(View.VISIBLE);
            loadFriendList();
        }
    }

    private void loadFriendList() {
        serverConnector.makeGetRequest("/controller/friends/list", new ServerConnector.FetchDataCallback() {
            @Override
            public void onSuccess(String jsonResponse) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    List<String> friendNames = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        friendNames.add(jsonArray.getString(i));
                    }

                    runOnUiThread(() -> friendAdapter.updateData(friendNames));

                } catch (JSONException e) {
                    Log.e(TAG, "Errore parsing JSON amici: " + e.getMessage());
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Errore caricamento amici: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(LobbyActivity.this, "Errore caricamento amici", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void startGame() {
        isRunning = false;
        Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }*/

}

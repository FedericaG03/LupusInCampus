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

        btnExit.setOnClickListener(v -> {
            isRunning = false;
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

    }


}

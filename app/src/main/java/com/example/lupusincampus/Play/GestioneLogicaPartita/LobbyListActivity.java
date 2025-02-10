package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.API.LobbyAPI;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;

public class LobbyListActivity extends BaseActivity {

    private LobbyAdapter adapter;
    private LobbyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostra_lobby_view);

        ListView listView = findViewById(R.id.my_list_view_lobby);
        dbHelper = new LobbyDatabaseHelper(this);

        // Richiede le lobby al server
        LobbyAPI lobbyAPI = new LobbyAPI();
        lobbyAPI.doShowLoddy(getApplicationContext());

        // Recupera i dati salvati nel database
        Cursor cursor = dbHelper.getAllLobbies();
        adapter = new LobbyAdapter(this, cursor);
        listView.setAdapter(adapter);

        // Aggiunge un header alla lista
        LayoutInflater inflater = getLayoutInflater();
        View headerView = inflater.inflate(R.layout.lobby_list_header, listView, false);
        listView.addHeaderView(headerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aggiorna i dati quando l'activity torna visibile
        Cursor cursor = dbHelper.getAllLobbies();
        adapter = new LobbyAdapter(this, cursor);
        ListView listView = findViewById(R.id.my_list_view_lobby);
        listView.setAdapter(adapter);
    }
}

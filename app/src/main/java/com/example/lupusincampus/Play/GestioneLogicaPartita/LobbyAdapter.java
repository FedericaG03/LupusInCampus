package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lupusincampus.R;
import com.example.lupusincampus.API.LobbyAPI;

public class LobbyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;

    public LobbyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getInt(cursor.getColumnIndexOrThrow("code"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lobby_item, parent, false);
        }

        // Recupero delle informazioni dal database
        cursor.moveToPosition(position);
        String creatorID = "Creatore: " + cursor.getInt(cursor.getColumnIndexOrThrow("creatorID"));
        String state = cursor.getString(cursor.getColumnIndexOrThrow("state"));
        String players = cursor.getInt(cursor.getColumnIndexOrThrow("numPlayer")) + "/" + "18";

        // Associazione ai TextView nel layout
        TextView creatorText = convertView.findViewById(R.id.creator);
        TextView statusText = convertView.findViewById(R.id.status);
        TextView playersText = convertView.findViewById(R.id.players);
        TextView joinBtn = convertView.findViewById(R.id.btnJoin);

        creatorText.setText(creatorID);
        statusText.setText(state);
        playersText.setText(players);

        // Recupero l'ID della lobby (presupponendo che "code" sia il campo dell'ID univoco della lobby)
        final int lobbyId = cursor.getInt(cursor.getColumnIndexOrThrow("code"));

        // Aggiungi listener per il pulsante "UNISCITI"
        joinBtn.setOnClickListener(v -> {
            Log.d("LobbyAdapter", "Bottone Unisciti premuto per lobby ID: " + lobbyId);

            // Chiamata API per unirsi alla lobby
            LobbyAPI lobbyAPI = new LobbyAPI();
            lobbyAPI.doJoinLobby(context, lobbyId);
        });

        return convertView;
    }
}

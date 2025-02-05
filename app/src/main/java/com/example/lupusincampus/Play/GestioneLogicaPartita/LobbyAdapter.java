package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lupusincampus.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LobbyAdapter extends ArrayAdapter<JSONObject> {
    private final Activity context;
    private final JSONObject[] lobbyData;

    public LobbyAdapter(Activity context, JSONObject[] lobbyData) {
        super(context, R.layout.lobby_view, lobbyData);
        this.context = context;
        this.lobbyData = lobbyData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.lobby_view, null, true);

        // Ottieni i dati dal JSON per la lobby
        JSONObject lobby = lobbyData[position];

        try {
            TextView creatorTextView = listItemView.findViewById(R.id.creator);
            TextView statusTextView = listItemView.findViewById(R.id.status);
            TextView playersTextView = listItemView.findViewById(R.id.players);
            TextView btnJoinTextView = listItemView.findViewById(R.id.btnJoin);

            // Imposta i dati nelle rispettive TextView
            creatorTextView.setText(lobby.getString("creator"));
            statusTextView.setText(lobby.getString("status"));
            playersTextView.setText(lobby.getString("players"));

            // Aggiungi il comportamento per il pulsante "Unisciti"
            btnJoinTextView.setOnClickListener(v -> {
                try {
                    Toast.makeText(context, "Unito alla lobby: " + lobby.getString("creator"), Toast.LENGTH_SHORT).show();
                    //TODO: CODICE PER ENTRARE IN LOBBY

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listItemView;
    }
}


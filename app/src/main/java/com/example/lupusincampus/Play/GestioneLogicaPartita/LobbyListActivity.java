package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;

public class LobbyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostra_lobby_view);

        ListView listView = findViewById(R.id.my_list_view_lobby);

        LayoutInflater inflater = getLayoutInflater();
        View headerView = inflater.inflate(R.layout.lobby_list_header, listView, false);
        listView.addHeaderView(headerView);

        ServerConnector serverConnector = new ServerConnector();
        // mostra lobby attive endpoint = controller/lobby/active-public-lobbies
        //GET
        //MI MANDI

        // Richiedi i dati dal server
        serverConnector.fetchDataForListView(this,new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object jsonResponse) {

            }

            @Override
            public void onError(String jsonResponse) {

            }

            @Override
            public void onServerError(Exception e) {

            }

            /*@Override
            public void onSuccess(String jsonResponse) {
                runOnUiThread(() -> {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        JSONObject[] lobbyData = new JSONObject[jsonArray.length()];

                        // Converti la risposta JSON in un array di JSONObject
                        for (int i = 0; i < jsonArray.length(); i++) {
                            lobbyData[i] = jsonArray.getJSONObject(i);
                        }

                        // Crea l'adapter e imposta i dati
                        LobbyAdapter adapter = new LobbyAdapter(LobbyListActivity.this, lobbyData);
                        listView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LobbyListActivity.this, "Errore nel parsing dei dati", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(LobbyListActivity.this, "Errore durante il fetch dei dati", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onServerOffline(Exception e) {
                Toast.makeText(LobbyListActivity.this, "Errore connessione server", Toast.LENGTH_SHORT).show();
            }*/
        });
    }
}

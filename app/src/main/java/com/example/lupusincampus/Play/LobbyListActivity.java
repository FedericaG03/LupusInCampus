package com.example.lupusincampus.Play;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LobbyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostra_lobby_view);

        ListView listView = findViewById(R.id.my_list_view_lobby);
        ServerConnector serverConnector = new ServerConnector();

        // Richiedi i dati dal server
        serverConnector.fetchDataForListView(new ServerConnector.FetchDataCallback() {
            @Override
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
        });
    }
}

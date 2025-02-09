package com.example.lupusincampus.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lupusincampus.Play.GestioneLogicaPartita.LobbyDatabaseHelper;
import com.example.lupusincampus.ServerConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class LobbyAPI {
    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "LobbyAPI";


    public void showLobbyRequest(Context ctx, ServerConnector.CallbackInterface callback){
        serverConnector.makeGetRequest(ctx,"/controller/lobby/active-public-lobbies", callback);
    }
    public void doShowLoddy(Context ctx) {
        Log.d(TAG, "doShowLoddy: Richiesta al server per le lobby");

        showLobbyRequest(ctx, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try{

                    JSONArray jsonResponse = (JSONArray) response;
                    // Crea un'istanza di LobbyDatabaseHelper e salva i dati
                    LobbyDatabaseHelper dbHelper = new LobbyDatabaseHelper(ctx);

                    for(int i = 0; i < jsonResponse.length(); i++){
                        JSONObject lobbies = ((JSONObject)jsonResponse.get(i)).getJSONObject("lobby");
                        int code = lobbies.getInt("code");
                        int creatorID = lobbies.getInt("creatorID");
                        String creationDate = lobbies.getString("creationDate");
                        int minNumPlayer = lobbies.getInt("minNumPlayer");
                        int numPlayer = lobbies.getInt("numPlayer");
                        int maxNumPlayer = lobbies.getInt("maxNumPlayer");
                        String type = lobbies.getString("type");
                        String state = lobbies.getString("state");

                        dbHelper.insertLobby(code, creatorID, creationDate, minNumPlayer, numPlayer, maxNumPlayer, type, state);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }

            @Override
            public void onError(String jsonResponse) {
                Toast.makeText(ctx, jsonResponse, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Toast.makeText(ctx, "Il server non risponde , errore nel caricamento delle lobby!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


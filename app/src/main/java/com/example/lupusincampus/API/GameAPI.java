package com.example.lupusincampus.API;

import android.content.Context;
import android.widget.Toast;

import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.ServerConnector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GameAPI {
    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "GameAPI";


    public void saveGameFinished(Context ctx,String codeLobby, ServerConnector.CallbackInterface callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("codeLobby", codeLobby);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makePostRequest(ctx, "/controller/game/save-lobby-game-state", jsonObject, callback);
    }

    public void doSaveGameFinished(Context context, String codeLobby){
        saveGameFinished(context, codeLobby, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object jsonResponse) {

            }

            @Override
            public void onError(String jsonResponse) {
                Toast.makeText(context, jsonResponse, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Toast.makeText(context, "Errore ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

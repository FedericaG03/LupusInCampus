package com.example.lupusincampus.API;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.Play.GestioneLogicaPartita.PartitaActivity;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GameAPI {
    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "GameAPI";


    public void startGame(Context ctx,String codeLobby, ServerConnector.CallbackInterface callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("codeLobby", codeLobby);
            jsonObject.put("playerID", String.valueOf(SharedActivity.getInstance(ctx).getId()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makePostRequest(ctx, "/controller/game/start-game", jsonObject, callback);
    }

    public void doStartGame(Context context, String codeLobby){
        startGame(context, codeLobby, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object jsonResponse) {

                try {
                    JSONObject player = ((JSONObject) jsonResponse).getJSONObject("player");
                    Intent intent = new Intent(context, PartitaActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("role", player.getString("role"));
                    Log.d(TAG, "onClick: vado alla partita, iniziamo a giocare: " + intent.toString());
                    context.startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

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

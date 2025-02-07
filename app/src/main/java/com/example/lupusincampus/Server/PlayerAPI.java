package com.example.lupusincampus.Server;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;



public class PlayerAPI  {
    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "PlayerAPI";
    private static volatile boolean FLAG = false;

    /**
     * Questo metodo si occupa di effettuare la richiesta di login al server e di interpretare la risposta
     * @param email
     * @param hashPass
     * @param ctx
     * @param sharedActivity
     * @return True se l'utente logga nel sistema correttamente, false altrimenti
     */
    public boolean doLogin(String email, String hashPass, Context ctx, SharedActivity sharedActivity) {

        loginRequest(ctx, email, hashPass, new ServerConnector.CallbackInterface() {

            @Override
            public void onSuccess(JSONObject infoPlayer) {
                try {
                    Log.d(TAG, "Risposta completa dal server: " + infoPlayer.toString());
                    // Secondo oggetto JSON con informazioni sul player
                    infoPlayer = infoPlayer.getJSONObject("player");
                    String storedHash = infoPlayer.getString("password"); // Password hashata

                    // Solo in caso di LOGIN_SUCCESS confrontiamo la password
                    if (hashPass.equals(storedHash)) {
                        sharedActivity.setEmail(infoPlayer.getString("email")); // Salva email
                        Log.i(TAG, "onSuccess: Imposto logged a true");
                        //sharedActivity.setLoggedIn(true);
                        FLAG = true;
                        Log.d(TAG, "onSuccess: La FLAG è: " + FLAG);
                    } else {
                        Toast.makeText(ctx, "Credenziali errate!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Toast.makeText(ctx, "Errore di connessione al server", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onServerError: " + e);
            }
        });
        return FLAG;
    }

    /**
     * Richiesta di login
     */
    //Mando dati al server e faccio richiesta
    public void loginRequest(Context ctx, String email, String password, ServerConnector.CallbackInterface callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            Log.d("DEBUG", "JSON inviato: " + jsonBody.toString());

            serverConnector.makePostRequest(ctx, "/controller/player/login", jsonBody, callback);
        } catch (JSONException e) {
            Log.e("ERRORE JSON", e.toString());
            callback.onServerError(e);
        }
    }

}

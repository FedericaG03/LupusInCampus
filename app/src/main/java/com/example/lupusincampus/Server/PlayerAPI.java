package com.example.lupusincampus.Server;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.lupusincampus.Login.LoginActivity;
import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class PlayerAPI {
    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "PlayerAPI";
    private static boolean FLAG = false;


    /**
     * Questo metodo si occupa di effettuare la richiesta di login al server e di interpretare la risposta
     *
     * @param email
     * @param hashPass
     * @param ctx
     * @param sharedActivity
     * @return True se l'utente logga nel sistema correttamente, false altrimenti
     */
    public boolean doLogin(String email, String hashPass, Context ctx, SharedActivity sharedActivity) {

        Log.d(TAG, "Avvio richiesta di login per: " + email);

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
                        sharedActivity.setLoggedIn(true);
                        FLAG = true;
                        Log.d(TAG, "Login riuscito per: " + email);
                        Intent intent = new Intent(ctx, MainActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                    } else {
                        Log.d(TAG, "Password errata per: " + email);
                        Toast.makeText(ctx, "Credenziali errate!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "Errore nel parsing JSON", e);
                    throw new RuntimeException(e);
                } finally {
                    Log.d(TAG, "onSuccess: Latch rilasciato dopo successo ");
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

            Log.d(TAG, "JSON inviato: " + jsonBody.toString());

            serverConnector.makePostRequest(ctx, "/controller/player/login", jsonBody, callback);
        } catch (JSONException e) {
            Log.e("ERRORE JSON", e.toString());
            callback.onServerError(e);
        }
    }


    /**
     * Questo metodo si occupa di effettuare la richiesta di registrazione al server e di interpretare la risposta
     *
     * @param email
     * @param hashPass
     * @param nickname
     * @param ctx
     * @param sharedActivity
     */
    public void doRegister(String email, String hashPass, String nickname, Context ctx, SharedActivity sharedActivity) {

        Log.d(TAG, "Avvio richiesta di registrazione per: " + email + nickname + hashPass);

        registerRequest(ctx, nickname, email, hashPass, new ServerConnector.CallbackInterface() {

            @Override
            public void onSuccess(JSONObject infoPlayer) {
                try {
                    Log.d(TAG, "Risposta completa dal server: " + infoPlayer.toString());
                    // Secondo oggetto JSON con informazioni sul player
                    infoPlayer = infoPlayer.getJSONObject("player");

                    Log.i(TAG, "onSuccess: Imposto logged a true");
                    sharedActivity.setEmail(infoPlayer.getString("email")); // Salva email
                    sharedActivity.setNickname(infoPlayer.getString("nickname")); // Salva nick
                    sharedActivity.setLoggedIn(true);

                    Intent intent = new Intent(ctx, MainActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);

                } catch (JSONException e) {
                    Log.d(TAG, "Errore nel parsing JSON", e);
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
    }

    public void doForgotPassword(String email, Context ctx) {
        Log.d(TAG, "Avvio richiesta cambio password per: " + email);

        recoverPasswordRequest(ctx, email, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(JSONObject infoPlayer) {
                Toast.makeText(ctx, "Controllare le email", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onSuccess: Email inviata al server per il recupero password, test, vado alla login");
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
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
    }

    /**
     * Richiesta di registrazione
     *
     * @param ctx
     * @param nickname
     * @param email
     * @param callback
     */
    public void registerRequest(Context ctx, String nickname, String email, String password, ServerConnector.CallbackInterface callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nickname", nickname);
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            serverConnector.makePutRequest(ctx, "/controller/player/registration", jsonBody, callback);
        } catch (JSONException e) {
            Log.e("ERRORE JSON", e.toString());
            callback.onServerError(e);
        }
    }

    /**
     * Recupero password, (invio mail al server)
     */
    public void recoverPasswordRequest(Context ctx, String email, ServerConnector.CallbackInterface callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);

            serverConnector.makePostRequest(ctx, "/controller/player/forgot-password", jsonBody, callback);
        } catch (JSONException e) {
            callback.onServerError(e);
        }
    }

    public void registerRequest(Context ctx, ServerConnector.CallbackInterface callback) {
        serverConnector.makeGetRequest(ctx, "/controller/player/logout", callback);
    }

    public void doLogout(Context ctx, SharedActivity sharedActivity) {
        Log.d(TAG, "Avvio richiesta di logout");

        registerRequest(ctx, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                Log.d("MainActivity", "Effettuato logout con successo");
                Toast.makeText(ctx, "Effettuato logout con successo!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String jsonResponse) {
                Log.d(TAG, "onError: Impossibile effettuare logout!");
                Toast.makeText(ctx, "Impossibile effettuare logout!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServerError(Exception e) {
                Log.d(TAG, "onError: Impossibile effettuare logout! Problema del serve");
                Toast.makeText(ctx, "Errore nel server impossibile effettuare logout!", Toast.LENGTH_LONG).show();
                sharedActivity.setLoggedIn(false);
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

    }
}

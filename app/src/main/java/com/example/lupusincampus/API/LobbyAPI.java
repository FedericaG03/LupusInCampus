package com.example.lupusincampus.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lupusincampus.Play.GestioneLogicaPartita.LobbyDatabaseHelper;
import com.example.lupusincampus.ServerConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LobbyAPI {
    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "LobbyAPI";

    /**
     * Effettua una richiesta GET al server per ottenere la lista delle lobby pubbliche attive.
     *
     * @param ctx      Contesto dell'applicazione.
     * @param callback Interfaccia di callback per gestire la risposta del server.
     */
    public void showLobbyRequest(Context ctx, ServerConnector.CallbackInterface callback){
        serverConnector.makeGetRequest(ctx,"/controller/lobby/active-public-lobbies", callback);
    }
    /**
     * Recupera le lobby pubbliche attive dal server e le salva nel database locale.
     * Se la richiesta ha successo, aggiorna il database locale con le lobby ricevute.
     *
     * @param ctx Contesto dell'applicazione.
     */
    public void doShowLoddy(Context ctx) {
        Log.d(TAG, "doShowLoddy: Richiesta al server per le lobby");

        showLobbyRequest(ctx, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try{

                    JSONArray jsonResponse = (JSONArray) response;
                    // Crea un'istanza di LobbyDatabaseHelper e salva i dati
                    LobbyDatabaseHelper dbHelper = new LobbyDatabaseHelper(ctx);
                    dbHelper.clearLobbies();

                    for(int i = 0; i < jsonResponse.length(); i++){
                        JSONObject lobbies = ((JSONObject)jsonResponse.get(i)).getJSONObject("lobby");
                        int code = lobbies.getInt("code");
                        int creatorID = lobbies.getInt("creatorID");
                        String creationDate = lobbies.getString("creationDate");
                        int numPlayer = lobbies.getInt("numPlayer");
                        String type = lobbies.getString("type");
                        String state = lobbies.getString("state");

                        dbHelper.insertLobby(code, creatorID, creationDate, numPlayer, type, state);
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

    /**
     * Effettua una richiesta al server per creare una nuova lobby.
     *
     * @param ctx      Contesto dell'applicazione.
     * @param tipo     Tipo di lobby da creare.
     * @param callback Interfaccia di callback per gestire la risposta del server.
     */
    public void createLobby(Context ctx, String tipo, ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tipo", tipo);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makePutRequest(ctx, "/controller/lobby/create-lobby", jsonObject, callback);
    }

    /**
     * Effettua una richiesta al server per eliminare una lobby esistente.
     *
     * @param ctx      Contesto dell'applicazione.
     * @param code     Codice identificativo della lobby da eliminare.
     * @param callback Interfaccia di callback per gestire la risposta del server.
     */
    public void deleteLobby(Context ctx, int code, ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makeDeleteRequest(ctx, "/controller/lobby/delete-lobby", jsonObject, callback);
    }

    /**
     * Effettua una richiesta al server per entrare in una lobby esistente.
     *
     * @param ctx      Contesto dell'applicazione.
     * @param code     Codice identificativo della lobby a cui unirsi.
     * @param callback Interfaccia di callback per gestire la risposta del server.
     */
    public void joinLobby(Context ctx, int code ,ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        serverConnector.makePostRequest(ctx, "/controller/lobby/join-lobby", jsonObject, callback);
    }


    public void doJoinLobby(Context ctx, int code) {
        Log.d(TAG, "doJoinLobby: Richiesta al server per le JOIN LOBBY");

        joinLobby(ctx,code, new ServerConnector.CallbackInterface() {

            @Override
            public void onSuccess(Object response) {
                try {
                    JSONObject jsonResponse = (JSONObject) response;
                    String status = jsonResponse.getString("status");

                    // Aggiorna il numero di giocatori nel database

                    // TODO: - sottoscrivere al websocket della lobby e della chat

                    int numPlayer = jsonResponse.getJSONArray("players").length();  // Prendi il numero di giocatori dalla risposta

                    // Aggiorna il database locale con il nuovo numero di giocatori
                    LobbyDatabaseHelper dbHelper = new LobbyDatabaseHelper(ctx);
                    dbHelper.updateNumPlayer(code, numPlayer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String jsonResponse) {
                Toast.makeText(ctx, "Errore nell'unirsi alla lobby: " + jsonResponse, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Toast.makeText(ctx, "Errore di connessione con il server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Effettua una richiesta al server per uscire da una lobby.
     *
     * @param ctx      Contesto dell'applicazione.
     * @param codeLobby Codice identificativo della lobby da abbandonare.
     * @param callback Interfaccia di callback per gestire la risposta del server.
     */
    public void leaveLobby(Context ctx, int codeLobby, ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("codeLobby", codeLobby);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        serverConnector.makePostRequest(ctx, "/controller/lobby/leave-lobby", jsonObject, callback);
    }

    /**
     * Effettua una richiesta al server per modificare una lobby esistente.
     *
     * @param ctx        Contesto dell'applicazione.
     * @param code       Codice identificativo della lobby da modificare.
     * @param minPlayers Numero minimo di giocatori richiesto.
     * @param maxPlayers Numero massimo di giocatori consentito.
     * @param callback   Interfaccia di callback per gestire la risposta del server.
     */
    public void modifyLobby(Context ctx, int code, int minPlayers, int maxPlayers, ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("codeLobby", code);
            jsonObject.put("minNumPlayer", minPlayers);
            jsonObject.put("maxNumPlayer", maxPlayers);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        serverConnector.makePostRequest(ctx, "/controller/lobby/modify-lobby", jsonObject, callback);
    }

    /**
     * Effettua una richiesta al server per invitare un amico in una lobby.
     *
     * @param ctx      Contesto dell'applicazione.
     * @param friendId ID dell'amico da invitare alla lobby.
     * @param callback Interfaccia di callback per gestire la risposta del server.
     */
    public void inviteFriendToLobby(Context ctx, int friendId, ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friendId", friendId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makePostRequest(ctx, "/controller/lobby/invite-friend-lobby", jsonObject, callback);
    }

    /**
     * Effettua una richiesta al server per creare una nuova lobby e aggiorna il database locale.
     *
     * @param ctx  Contesto dell'applicazione.
     * @param tipo Tipo di lobby da creare.
     */
    public void doCreateLobby(Context ctx, String tipo) {
        Log.d(TAG, "doCreateLobby: Richiesta al server per creare una nuova lobby");

        createLobby(ctx, tipo, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try {
                    JSONObject jsonResponse = (JSONObject) response;

                    // Estrai i dettagli della lobby creata
                    JSONObject lobby = jsonResponse.getJSONObject("lobby");
                    int code = lobby.getInt("code");
                    int creatorID = lobby.getInt("creatorID");
                    String creationDate = lobby.getString("creationDate");
                    //inizialmente settato ad uno
                    int numPlayer = lobby.getInt("numPlayer");
                    String type = lobby.getString("type");
                    String state = lobby.getString("state");


                    // TODO: - sottoscrivere al websocket della lobby e della chat

                    // Salva la lobby nel database locale
                    LobbyDatabaseHelper dbHelper = new LobbyDatabaseHelper(ctx);
                    dbHelper.insertLobby(code, creatorID, creationDate, numPlayer, type, state);

                    Log.d(TAG, "doCreateLobby: Lobby creata e salvata nel database");

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
                Toast.makeText(ctx, "Errore nella creazione della lobby, il server non risponde!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


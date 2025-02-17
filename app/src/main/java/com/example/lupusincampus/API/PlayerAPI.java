package com.example.lupusincampus.API;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.makeRestartActivityTask;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.example.lupusincampus.Login.LoginActivity;
import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.Model.Game;
import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.Play.GestioneLogicaPartita.CreateLobbyActivity;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import me.pushy.sdk.lib.jackson.core.JsonParseException;
import me.pushy.sdk.lib.jackson.databind.JsonMappingException;
import me.pushy.sdk.lib.jackson.databind.ObjectMapper;

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
            public void onSuccess(Object response) {
                try {
                    JSONObject infoPlayer = (JSONObject) response;
                    Log.d(TAG, "Risposta completa dal server: " + infoPlayer.toString());
                    // Secondo oggetto JSON con informazioni sul player
                    infoPlayer = infoPlayer.getJSONObject("player");
                    String storedHash = infoPlayer.getString("password"); // Password hashata

                    // Solo in caso di LOGIN_SUCCESS confrontiamo la password
                    if (hashPass.equals(storedHash)) {
                        sharedActivity.setEmail(infoPlayer.getString("email")); // Salva email
                        sharedActivity.setNickname(infoPlayer.getString("nickname"));
                        Log.d(TAG, "onSuccess: nickname is:" + sharedActivity.getNickname());
                        sharedActivity.setLoggedIn(true);
                        sharedActivity.setId(Integer.parseInt(infoPlayer.getString("id")));
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
            public void onSuccess(Object response) {
                try {
                    JSONObject infoPlayer = (JSONObject) response;
                    Log.d(TAG, "Risposta completa dal server: " + infoPlayer.toString());
                    // Secondo oggetto JSON con informazioni sul player
                    infoPlayer = infoPlayer.getJSONObject("player");

                    Log.i(TAG, "onSuccess: Imposto logged a true" + infoPlayer.getString("nickname"));
                    sharedActivity.setEmail(infoPlayer.getString("email")); // Salva email
                    sharedActivity.setNickname(infoPlayer.getString("nickname")); // Salva nick
                    sharedActivity.setId(Integer.parseInt(infoPlayer.getString("id")));
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
            public void onSuccess(Object infoPlayer) {
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

    public void logoutRequest(Context ctx, ServerConnector.CallbackInterface callback) {serverConnector.makeGetRequest(ctx, "/controller/player/logout", callback);}
    public void doLogout(Context ctx, SharedActivity sharedActivity) {
        Log.d(TAG, "Avvio richiesta di logout");

        logoutRequest(ctx, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object jsonResponse) {
                Log.d(TAG, "Effettuato logout con successo");
                Toast.makeText(ctx, "Effettuato logout con successo!", Toast.LENGTH_SHORT).show();
                sharedActivity.setLoggedIn(false);
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }

            @Override
            public void onError(String jsonResponse) {
                Log.d(TAG, "onError: Impossibile effettuare logout!");
                Toast.makeText(ctx, "Impossibile effettuare logout!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);

            }

            @Override
            public void onServerError(Exception e) {
                Log.d(TAG, "onError: Impossibile effettuare logout! Problema del serve");
                Toast.makeText(ctx, "Errore nel server impossibile effettuare logout!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void doChangeName(String nickname, Context ctx){
        Log.d(TAG, "doChangeName: invia richiesta al server");
        changeNameRequest(nickname,ctx, new ServerConnector.CallbackInterface(){

            @Override
            public void onSuccess(Object response) {
                try {
                    JSONObject jsonResponse = (JSONObject) response;
                    JSONObject infoPlayer = jsonResponse.getJSONObject("player");
                    Log.d(TAG, "onSuccess: nickname cambiato in: " + infoPlayer.getString("nickname"));
                    SharedActivity.getInstance(ctx).setNickname(infoPlayer.getString("nickname"));
                    Toast.makeText(ctx, "Nickname aggiornato in:" + infoPlayer.getString("nickname"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ctx,"Errore nella risposta dal server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changeNameRequest(String nickname,Context ctx, ServerConnector.CallbackInterface callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("newNickname", nickname);

            serverConnector.makePostRequest(ctx, "/controller/player-area/edit-player-data", jsonBody, callback);
        } catch (JSONException e) {
            Log.e("ERRORE JSON", e.toString());
            callback.onServerError(e);
        }
    }

    public void doDelete(int id, Context ctx){
        Log.d(TAG, "doDelete: Cancellazione account");

        deleteRequest(ctx, id, new ServerConnector.CallbackInterface(){

            @Override
            public void onSuccess(Object jsonResponse) {
                Log.d(TAG, "Cancellazione utente");
                Toast.makeText(ctx, "Utente cancellato!", Toast.LENGTH_SHORT).show();
                SharedActivity.getInstance(ctx).setLoggedIn(false);
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }

            @Override
            public void onError(String jsonResponse) {
                Log.d(TAG, "Errore ancellazione utente ");
                Toast.makeText(ctx, "Utente non cancellato!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Log.d(TAG, "onError: Impossibile cancellare l'utente! Problema del serve");
                Toast.makeText(ctx, "Errore nel server impossibile cancellare l'account!", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void deleteRequest(Context ctx, int id, ServerConnector.CallbackInterface callback){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);

            serverConnector.makePostRequest(ctx, "/controller/player/delete", jsonBody, callback);
        } catch (JSONException e) {
            Log.e("ERRORE JSON", e.toString());
            callback.onServerError(e);
        }
    }


    public void requestPlayetAreaInfo(Context ctx, ServerConnector.CallbackInterface callback){serverConnector.makeGetRequest(ctx,"/controller/player-area/my", callback);}
    public void doGetPlayerAreaInfo(Context context) {
        requestPlayetAreaInfo(context, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try {
                    JSONArray jsonArray = (JSONArray) response;
                    JSONObject jsonPlayer = (JSONObject) jsonArray.get(0);
                    JSONObject infoPlayer = jsonPlayer.getJSONObject("player");
                    JSONArray gamePartecipated = ((JSONObject)jsonArray.get(1)).getJSONArray("GamePartecipated");
                    JSONArray pendingRequests = ((JSONObject)jsonArray.get(2)).getJSONArray("PendingFriendRequest");

                    List<Game> games = new ArrayList<>();
                    List<Player> playerList = new ArrayList<>();
                    ObjectMapper objectMapper = new ObjectMapper();

                    // Parsing della lista delle partite
                    for (int i = 0; i < gamePartecipated.length(); i++) {
                        JSONObject gameJson = gamePartecipated.getJSONObject(i);

                        // Chiamata alla funzione parseGame per ottenere l'oggetto Game
                        Game game = parseGame(gameJson);
                        Log.d(TAG, "Game " + i + ": " + game.toString());
                        if (game != null) {
                            games.add(game);
                            Log.d(TAG, "Game " + i + ": " + game.toString());
                        }
                    }


                    // Parsing della lista degli amici
                    for (int i = 0; i < pendingRequests.length(); i++) {
                        JSONObject friendJson = pendingRequests.getJSONObject(i);
                        Player friend = objectMapper.readValue(friendJson.toString().substring(10), Player.class);
                        playerList.add(friend);
                    }


                    SharedActivity sharedActivity = SharedActivity.getInstance(context);
                    sharedActivity.setGameList(games);
                    sharedActivity.setPlayerRequestList(playerList);

                    Log.d("PlayerAPI", "Salvati " + games.size() + " giochi e " + playerList.size() + " amici in SharedActivity");

                    Log.d(TAG, "onSuccess: infoPlayer: " + infoPlayer.toString());
                    Log.d(TAG, "onSuccess: gamePartecipated: " + gamePartecipated.toString());
                    Log.d(TAG, "onSuccess: pendingRequests: " + pendingRequests.toString());

                }catch(JSONException  e){
                    Log.e(TAG, "onSuccess: ", e);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonParseException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(String jsonResponse) {
                Log.d(TAG, "onError: Errore doGetPlayerAreaInfo");
                Toast.makeText(context,jsonResponse,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Log.e(TAG, "onServerError: ");
                Toast.makeText(context,"Errore nel server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Game parseGame(JSONObject gameJson) {
        try {
            // Parsing dei dati dalla JSONObject game
            JSONObject games = gameJson.getJSONObject("Game");
            int id = games.getInt("id");
            int creatorId = games.getInt("creatorId");
            int winningPlayerId = games.getInt("winningPlayerId");
            String gameDate = games.getString("gameDate");

            // Parsing della data come LocalDateTime
            LocalDateTime localDateTime = LocalDateTime.parse(gameDate);

            // Parsing della lista di partecipanti
            List<Player> partecipants = new ArrayList<>();
            JSONArray playersArray = games.getJSONArray("participants");
            //dev leggere il plyer

            for (int j = 0; j < playersArray.length(); j++) {
                JSONObject playerJson = playersArray.getJSONObject(j);
                Player player = parsePlayerWithoutFrindsList(playerJson.getJSONObject("player"));
                if (player != null) {
                    partecipants.add(player);
                }
            }
            // Creazione dell'oggetto Game
            return new Game(id, creatorId, localDateTime, partecipants, winningPlayerId);

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing game data: " + e.getMessage());
            return null;
        }
    }

    // Metodo per il parsing del giocatore senza salvare la lista amici
    public Player parsePlayerWithoutFrindsList(JSONObject playerJson) {
        try {
            Player player = new Player();
            player.setId(playerJson.getInt("id"));
            player.setNickname(playerJson.getString("nickname"));
            player.setEmail(playerJson.getString("email"));  // Email
            player.setPassword(playerJson.getString("password"));  // Password
            player.setRole(playerJson.getString("role"));// Ruolo
            return player;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing player data: " + e.getMessage());
            return null;  // O gestisci l'errore come necessario
        }
    }


}

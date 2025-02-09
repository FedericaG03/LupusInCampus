package com.example.lupusincampus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lupusincampus.Model.Game;
import com.example.lupusincampus.Model.Player;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.Synchronized;

public class SharedActivity {
    private static final String TAG = "SharedActivity";
    private static final String USER_PREFS = "UserPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";
    private static final String SESSION_ID = "sessionId";
    private static final String PUSHY_TOKEN = "pushy_token";
    private static final String ID = "id";

    private static SharedActivity instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    //Liste per amici e partite
    private List<Player> playerRequestList = new ArrayList<>();
    private List<Player> playerList = new ArrayList<>();
    private List<Game> gameList = new ArrayList<>();


    // Singleton thread-safe con lazy initialization
    public static synchronized SharedActivity getInstance(Context context) {
        if (instance == null) {
            instance = new SharedActivity(context);
        }
        return instance;
    }

    public List<Player> getPlayerRequestList() {
        return playerRequestList;
    }

    public void setPlayerRequestList(List<Player> playerRequestList) {
        this.playerRequestList = playerRequestList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    private SharedActivity(Context context) {
        this.sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public synchronized void setId(int id) {
        editor.putInt(ID, id).apply();
    }

    public synchronized int getId() {
       return sharedPreferences.getInt(ID,-1);
    }

    // Imposta se l'utente è loggato
    public synchronized void setLoggedIn(boolean isLoggedIn) {
        Log.d(TAG, "setLoggedIn: " + isLoggedIn);
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn).apply();
    }

    // Restituisce se l'utente è loggato
    public synchronized boolean isLoggedIn() {
        boolean loggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
        Log.d(TAG, "isLoggedIn: " + loggedIn);
        return loggedIn;
    }

    // Salva i dettagli dell'utente (ID, nickname, email)
    public synchronized void saveUserDetails(String id, String nickname, String email) {
        Log.d(TAG, "saveUserDetails: ID=" + id + ", Nickname=" + nickname + ", Email=" + email);
        editor.putString("id", id)
                .putString(NICKNAME, nickname)
                .putString(EMAIL, email)
                .apply();
    }

    public synchronized String getNickname() {
        return sharedPreferences.getString(NICKNAME, "");
    }

    public synchronized void setNickname(String nickname) {
        saveString(NICKNAME, nickname);
    }

    public synchronized String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public synchronized void setEmail(String email) {
        saveString(EMAIL, email);
    }

    public synchronized String getToken() {
        return sharedPreferences.getString(TOKEN, "");
    }

    public synchronized void setToken(String token) {
        saveString(TOKEN, token);
    }

    public synchronized String getSessionId() {
        return sharedPreferences.getString(SESSION_ID, "");
    }

    public synchronized void setSessionId(String sessionId) {
        saveString(SESSION_ID, sessionId);
    }

    public synchronized void savePushyToken(String token) {
        saveString(PUSHY_TOKEN, token);
    }

    public synchronized String getPushyToken() {
        return sharedPreferences.getString(PUSHY_TOKEN, "");
    }

    // Metodo helper per salvare stringhe nelle SharedPreferences
    private synchronized void saveString(String key, String value) {
        Log.d(TAG, "saveString: " + key + " = " + value);
        editor.putString(key, value).apply();
    }

    // Cancella tutte le preferenze salvate
    public synchronized void clear() {
        Log.d(TAG, "clear: Resetting shared preferences");
        editor.clear().commit();
    }
}



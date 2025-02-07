package com.example.lupusincampus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import kotlin.jvm.Synchronized;

public class SharedActivity {
    private static final String TOKEN ="token";
    private static final String ID = "0";
    private static final String USER_PREFS = "UserPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email";
    private static final String SESSIONID = "sessionId";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static SharedActivity instance;

    public static SharedActivity getInstance(Context context){
        if(instance == null){
            instance = new SharedActivity(context);
        }
        return instance;
    }
    private SharedActivity(Context context) {
        this.sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.setLoggedIn(false);
    }

    public synchronized void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }


    public synchronized void setSessionid(String sessionid) {
        editor.putString(SESSIONID, sessionid);
        editor.apply();
    }

    public synchronized String getSessionid(){
        return sharedPreferences.getString(SESSIONID, null);
    }
    public synchronized boolean isLoggedIn() {
        Log.d("TAG", "isLoggedIn: " + this + ": " + sharedPreferences.toString());
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    //isLoggedIn: com.example.lupusincampus.SharedActivity@28e5058: android.app.SharedPreferencesImpl@f2335b1

    public synchronized void saveUserDetails(String id, String nickname, String email) {
        editor.putString(ID,id);
        editor.putString(NICKNAME, nickname);
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public synchronized String getNickname() {
        return sharedPreferences.getString(NICKNAME, null);
    }

    public synchronized String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public  synchronized void setEmail(String email) {
        editor.putString(EMAIL, email).apply();
    }

    public  synchronized void setNickname(String nickname) {
        editor.putString(NICKNAME, nickname).apply();
    }

    public  synchronized void setToken(String token){
        editor.putString(TOKEN, token).apply();
    }

    public  synchronized String getToken(){
        return sharedPreferences.getString(TOKEN,null);
    }

    public  synchronized void clear() {
        editor.clear();
        editor.apply();
    }


    //Per salvare il token Pushy
    public  synchronized void savePushyToken(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pushy_token", token);
        editor.apply();
    }
    
    public synchronized String getPushyToken() {
        return sharedPreferences.getString("pushy_token", null);
    }
}



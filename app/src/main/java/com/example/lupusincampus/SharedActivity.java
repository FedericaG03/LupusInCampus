package com.example.lupusincampus;

import android.content.Context;
import android.content.SharedPreferences;

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
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public void setSessionid(String sessionid) {
        editor.putString(SESSIONID, sessionid);
        editor.apply();
    }

    public String getSessionid(){
        return sharedPreferences.getString(SESSIONID, null);
    }
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void saveUserDetails(String id, String nickname, String email) {
        editor.putString(ID,id);
        editor.putString(NICKNAME, nickname);
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getNickname() {
        return sharedPreferences.getString(NICKNAME, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email).apply();
    }

    public void setNickname(String nickname) {
        editor.putString(NICKNAME, nickname).apply();
    }

    public void setToken(String token){
        editor.putString(TOKEN, token).apply();
    }

    public String getToken(){
        return sharedPreferences.getString(TOKEN,null);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }


    //Per salvare il token Pushy
    public void savePushyToken(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pushy_token", token);
        editor.apply();
    }
    
    public String getPushyToken() {
        return sharedPreferences.getString("pushy_token", null);
    }
}



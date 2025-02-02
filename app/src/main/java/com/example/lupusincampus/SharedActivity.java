package com.example.lupusincampus;

import android.content.Context;
import android.content.SharedPreferences;

import org.mindrot.jbcrypt.BCrypt;

public class SharedActivity {
    private static final String USER_PREFS = "UserPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String LOGIN_METHOD = "login_method"; // Aggiunto campo per email/nickname

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedActivity(Context context) {
        this.sharedPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void saveUserDetails(String nickname, String email, String password, String loginMethod) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // Cripta la password
        editor.putString(NICKNAME, nickname);
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, hashedPassword); // Salva la password criptata
        editor.putString(LOGIN_METHOD, loginMethod); // Salva il metodo di login
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

    public void setPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // Cripta la password
        editor.putString(PASSWORD, hashedPassword); // Salva la password criptata
        editor.apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "DEFAULT_PASSWORD");
    }

    public boolean checkPassword(String enteredPassword) {
        String storedHashedPassword = getPassword();
        return BCrypt.checkpw(enteredPassword, storedHashedPassword); // Verifica la password
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    public void setLoginMethod(String method) {
        editor.putString(LOGIN_METHOD, method).apply();
    }

    public String getLoginMethod() {
        return sharedPreferences.getString(LOGIN_METHOD, "UNKNOWN");
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



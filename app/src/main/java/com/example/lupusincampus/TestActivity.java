package com.example.lupusincampus;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.SharedActivity;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private PlayerAPI playerAPI;
    private SharedActivity sharedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Inizializzazione PlayerAPI e SharedActivity
        playerAPI = new PlayerAPI();
        sharedActivity = SharedActivity.getInstance(this);

        // Input campi
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        EditText inputNickname = findViewById(R.id.inputNickname);

        // Pulsanti
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnRecoverPassword = findViewById(R.id.btnRecoverPassword);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnChangeName = findViewById(R.id.btnChangeName);
        Button btnDeleteUser = findViewById(R.id.btnDeleteUser);
        Button btnGetPlayerInfo = findViewById(R.id.btnGetPlayerInfo);

        // Login
        btnLogin.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            playerAPI.doLogin(email, "ypeBEsobvcr6wjGzmiPcTaeG7/gUfE5yuYB3ha/uSLs=", this, sharedActivity);
        });

        // Registrazione
        btnRegister.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String nickname = inputNickname.getText().toString();
            playerAPI.doRegister(email, "ypeBEsobvcr6wjGzmiPcTaeG7/gUfE5yuYB3ha/uSLs=", nickname, this, sharedActivity);
        });

        // Recupero Password
        btnRecoverPassword.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            playerAPI.doForgotPassword(email, this);
        });

        // Logout
        btnLogout.setOnClickListener(v -> playerAPI.doLogout(this, sharedActivity));

        // Cambia Nome
        btnChangeName.setOnClickListener(v -> {
            String nickname = inputNickname.getText().toString();
            playerAPI.doChangeName(nickname, this);
        });

        // Cancella Utente
        btnDeleteUser.setOnClickListener(v -> {
            int id = sharedActivity.getId(); // Ottieni l'ID dal contesto condiviso
            playerAPI.doDelete(id, this);
        });

        // Ottieni Info Giocatore
        btnGetPlayerInfo.setOnClickListener(v -> playerAPI.doGetPlayerAreaInfo(this));
    }
}

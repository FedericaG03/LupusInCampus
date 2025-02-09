package com.example.lupusincampus.PlayerArea;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

public class PlayerAreaActivity extends AppCompatActivity {

    private TextView tvPlayerName;
    private Button btnDeleteAccount, btnChangeNickname, btnGameHistory, btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_area_activity);

        SharedActivity sharedActivity = SharedActivity.getInstance(this);

        // Collegamento degli elementi UI
        tvPlayerName = findViewById(R.id.tvPlayerName);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnChangeNickname = findViewById(R.id.btnChangeNickname);
        btnGameHistory = findViewById(R.id.btnGameHistory);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        // Recupero del nome giocatore dalle SharedPreferences
        tvPlayerName.setText(sharedActivity.getNickname());

        // Listener per il pulsante "Elimina Account"
        btnDeleteAccount.setOnClickListener(v -> {
            PlayerAPI playerAPI = new PlayerAPI();
            playerAPI.doDelete(sharedActivity.getId(),getApplicationContext());
        });

        // Listener per il pulsante "Modifica Nickname"
        btnChangeNickname.setOnClickListener(v -> {
            // Creazione di un AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cambia Nickname");

            // Creazione dell'EitText per l'input dell'utente
            final EditText input = new EditText(this);
            input.setHint("Inserisci nuovo nickname");
            builder.setView(input);

            // Bottone "OK" per confermare il cambiamento
            builder.setPositiveButton("OK", (dialog, which) -> {
                String newNickname = input.getText().toString().trim();
                if (!newNickname.isEmpty()) {
                    PlayerAPI playerAPI = new PlayerAPI();
                    playerAPI.doChangeName(newNickname, getApplicationContext());
                } else {
                    Toast.makeText(this, "Il nickname non può essere vuoto!", Toast.LENGTH_SHORT).show();
                }
            });

            // Bottone "Annulla"
            builder.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss());

            // Mostra il dialog
            builder.show();
        });



        // Listener per il pulsante "Storico Partite"
        btnGameHistory.setOnClickListener(v -> {
            PlayerAPI playerAPI = new PlayerAPI();
            //metodo da creare
            //playerAPI.doForgotPassword(sharedActivity.getEmail(), getApplicationContext());
        });

        // Listener per il pulsante "Cambio Password"
        btnChangePassword.setOnClickListener(v -> {
            PlayerAPI playerAPI = new PlayerAPI();
            playerAPI.doForgotPassword(sharedActivity.getEmail(), getApplicationContext());
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPlayerName.setText(SharedActivity.getInstance(getApplicationContext()).getNickname());
    }
}

package com.example.lupusincampus.PlayerArea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lupusincampus.PlayerArea.HistoryGames;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

public class PlayerAreaActivity extends AppCompatActivity implements ChangeNicknameDialogFragment.OnNicknameChangeListener {

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
            playerAPI.doDelete(sharedActivity.getId(), this); // Usa 'this' come contesto
        });

        // Listener per il pulsante "Modifica Nickname"
        btnChangeNickname.setOnClickListener(v -> {
            ChangeNicknameDialogFragment dialogFragment = new ChangeNicknameDialogFragment();
            dialogFragment.setOnNicknameChangeListener(this);
            dialogFragment.show(getSupportFragmentManager(), "changeNickname");
        });

        // Listener per il pulsante "Storico Partite"
        btnGameHistory.setOnClickListener(v -> {
            // Aggiungi un log per quando il pulsante viene cliccato
            Log.d("PlayerAreaActivity", "Pulsante 'Storico Partite' cliccato");

            Intent intent = new Intent(getApplicationContext(), HistoryGames.class);

            // Aggiungi un log per verificare che l'intento sia stato creato correttamente
            Log.d("PlayerAreaActivity", "Intent per HistoryGames creato: " + intent.toString());

            startActivity(intent);

            // Aggiungi un log per quando l'attività è stata avviata
            Log.d("PlayerAreaActivity", "Activity HistoryGames avviata");
        });

        // Listener per il pulsante "Cambio Password"
        btnChangePassword.setOnClickListener(v -> {
            PlayerAPI playerAPI = new PlayerAPI();
            playerAPI.doForgotPassword(sharedActivity.getEmail(), this); // Usa 'this' come contesto
        });
    }

    @Override
    public void onNicknameChanged(String newNickname) {
        if (!newNickname.isEmpty()) {
            PlayerAPI playerAPI = new PlayerAPI();
            playerAPI.doChangeName(newNickname, this); // Usa 'this' come contesto
            tvPlayerName.setText(newNickname);
            Toast.makeText(this, "Nickname cambiato con successo!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Il nickname non può essere vuoto!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPlayerName.setText(SharedActivity.getInstance(getApplicationContext()).getNickname());
    }
}

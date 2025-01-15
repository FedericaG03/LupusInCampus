package com.example.lupusincampus;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prima verifico se l'utente sia loggato
        SharedPreferences sharedPref = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);
        /*"userPrefs" è il nome del file in cui vengono memorizzate le preferenze. Questo nome può essere a tua scelta (come ad esempio userPrefs per le preferenze legate all'utente).
            MODE_PRIVATE significa che il file di preferenze è privato per questa applicazione. Nessun altro processo (o app) potrà leggere o scrivere queste preferenze.*/
        //non è loggato
        if (!isLoggedIn) {
            // Se non è loggato, vai alla LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Chiudi MainActivity per evitare che l'utente torni indietro
            return;     // Ferma l'esecuzione della funzione se l'utente non è loggato
        }

        String username = sharedPref.getString("username", "DEFAULT");


        ConstraintLayout mainLayout = findViewById(R.id.main_layout);
        ConstraintLayout sidebar = findViewById(R.id.profile_sidebar);



        TextView profileButtom = findViewById(R.id.probile_btn);
        profileButtom.setText(username);
        TextView playButton = findViewById(R.id.play_btn);
        TextView rulesButton = findViewById(R.id.rules_btn);
        TextView settingsButton = findViewById(R.id.setting_btn);
        TextView exitButton = findViewById(R.id.exit_btn);
        TextView friendsListButton = findViewById(R.id.lista_amici_btn);

        profileButtom.setOnClickListener( v->{
            if(profileButtom.getVisibility() == View.VISIBLE){
                profileButtom.setVisibility(View.GONE);
                sidebar.setVisibility(View.VISIBLE);
            }
        });

        mainLayout.setOnClickListener(v->{
            if(sidebar.getVisibility() == View.VISIBLE){
                profileButtom.setVisibility(View.VISIBLE);
                sidebar.setVisibility(View.GONE);
            }
        });

        exitButton.setOnClickListener(v->{
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Conferma uscita")
                    .setMessage("Sei sicuro di voler uscire dall'app?")
                    .setPositiveButton("Sì", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });

        friendsListButton.setOnClickListener(v->{
            Intent intent = new Intent(this, ListaAmiciActivity.class);
            startActivity(intent);
        });

        /*rulesButton.setOnClickListener(v->{
            Intent intent = new Intent(this, RegoleActivity.class);
            startActivity(intent);
        });*/
    }
    
}

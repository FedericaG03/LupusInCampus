package com.example.lupusincampus;

import com.example.lupusincampus.Login.LoginActivity;
import com.example.lupusincampus.Play.PlayActivity;
import com.example.lupusincampus.Amici.ListaAmiciActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    private SharedActivity sharedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedActivity = new SharedActivity(this); // Istanza della SharedActivity
        boolean isLoggedIn = sharedActivity.isLoggedIn();
        Log.d("LoginActivity", "isLoggedIn (all'avvio): " + isLoggedIn);

        if (!isLoggedIn) {
            Log.d("MainActivity", "Utente non loggato, reindirizzamento a LoginActivity");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }


        // Recupere nickname e password salvata
        String nickname = sharedActivity.getNickname();
        String mail = sharedActivity.getEmail();
        Log.d("MainActivity", "Utente loggato: " + nickname);

        ConstraintLayout mainLayout = findViewById(R.id.main_layout);
        ConstraintLayout sidebar = findViewById(R.id.profile_sidebar);


        TextView profileButton = findViewById(R.id.probile_btn);
        profileButton.setText(nickname);
        TextView playButton = findViewById(R.id.play_btn);
        TextView rulesButton = findViewById(R.id.rules_btn);
        TextView settingsButton = findViewById(R.id.setting_btn);
        TextView exitButton = findViewById(R.id.exit_btn);
        TextView friendsListButton = findViewById(R.id.lista_amici_btn);


        profileButton.setOnClickListener( v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.GONE);
                sidebar.setVisibility(View.VISIBLE);
            }
        });

        mainLayout.setOnClickListener(v->{
            if(sidebar.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.VISIBLE);
                sidebar.setVisibility(View.GONE);
            }
        });

        playButton.setOnClickListener(v -> {
            Log.d("MainActivity","Vado alla playactivity" );
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            startActivity(intent);
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

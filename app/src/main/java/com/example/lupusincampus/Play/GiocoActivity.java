package com.example.lupusincampus.Play;

import com.example.lupusincampus.CookieHelper;
import com.example.lupusincampus.Login.LoginActivity;
import com.example.lupusincampus.Amici.ListaAmiciActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class GiocoActivity extends AppCompatActivity {
    private SharedActivity sharedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        CookieHelper.init();

        sharedActivity = SharedActivity.getInstance(this);
        boolean isLoggedIn = sharedActivity.isLoggedIn();
        Log.d("LoginActivity", "isLoggedIn : " + isLoggedIn);

        /*if (!isLoggedIn) {
            Log.d("MainActivity", "Utente non loggato, reindirizzamento a LoginActivity");
            Intent intent = new Intent(GiocoActivity.this, LoginActivity.class);
            startActivity(intent);
        }*/


        // Recupere nickname e password salvata
        String nickname = sharedActivity.getNickname();
        String mail = sharedActivity.getEmail();
        Log.d("MainActivity", "Utente loggato: " + nickname);

        ConstraintLayout mainLayout = findViewById(R.id.main_layout);
        ConstraintLayout sidebar = findViewById(R.id.profile_sidebar);


        TextView profileButton = findViewById(R.id.probile_btn);
        TextView playButton = findViewById(R.id.play_btn);
        TextView rulesButton = findViewById(R.id.rules_btn);
        TextView settingsButton = findViewById(R.id.setting_btn);
        TextView exitButton = findViewById(R.id.exit_btn);
        TextView friendsListButton = findViewById(R.id.lista_amici_btn);
        TextView logoutButton = findViewById(R.id.logout_btn);
        TextView usernameSidebar = findViewById(R.id.username_sdb);

        profileButton.setText(nickname);
        usernameSidebar.setText(nickname);


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
            Intent intent = new Intent(GiocoActivity.this, WordActivity.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(v->{
            new AlertDialog.Builder(GiocoActivity.this)
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

        rulesButton.setOnClickListener(v->{
            //Intent intent = new Intent(this, RuoliActivity.class);
            //startActivity(intent);
        });

        logoutButton.setOnClickListener(v->{
            new ServerConnector().logoutReqeust(this,new ServerConnector.FetchDataCallback() {
                @Override
                public void onSuccess(String jsonResponse) {
                    Log.d("MainActivity", "Effettuato logout con successo");
                    Toast.makeText(GiocoActivity.this, "Effettuato logout con successo!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("MainActivity", "Impossibile effettuare logout!");
                    Toast.makeText(GiocoActivity.this, "Impossibile effettuare logout!" + e, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onServerOffline(Exception e) {
                    Toast.makeText(GiocoActivity.this, "Errore connessione server!" + e, Toast.LENGTH_SHORT).show();
                }
            });
            sharedActivity.setLoggedIn(false);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

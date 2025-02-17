package com.example.lupusincampus;

import com.example.lupusincampus.API.FriendAPI;
import com.example.lupusincampus.API.NotificationAPI;
import com.example.lupusincampus.Amici.ListaAmiciActivity;
import com.example.lupusincampus.Amici.ListaRichiesteAmiciActivity;
import com.example.lupusincampus.Login.LoginActivity;
import com.example.lupusincampus.Play.GestioneLogicaPartita.ChatActivity;
import com.example.lupusincampus.Play.GiocaActivity;
import com.example.lupusincampus.PlayerArea.PlayerAreaActivity;
import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.Regole.RegoleRuoliActivity;
import com.example.lupusincampus.Settings.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.pushy.sdk.Pushy;


public class MainActivity extends BaseActivity {
    private SharedActivity sharedActivity;
    private static final String TAG ="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedActivity = SharedActivity.getInstance(this);
        Log.d(TAG, "onCreate: sessioneID" + sharedActivity.getSessionId());

        if (!Pushy.isRegistered(this)){
            new NotificationAPI().doSave(this);

        } else {
            Log.d(TAG, "onCreate: " + Pushy.getDeviceCredentials(this).token);
        }

        Pushy.listen(this);


        if (sharedActivity.getSessionId() == null){
            CookieHelper.init();
        }
        else {
            boolean isLoggedIn = sharedActivity.isLoggedIn();
            Log.d("MainActivity", "isLoggedIn : " + isLoggedIn);

            if (!sharedActivity.isLoggedIn()) {
                Log.d("MainActivity", "Utente non loggato, chiudo MainActivity e passo a LoginActivity");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Chiude `MainActivity` per evitare loop
                return;
            }

            PlayerAPI playerAPI = new PlayerAPI();
            FriendAPI friendAPI = new FriendAPI();
            //request friends
            friendAPI.doGetFriendsList(getApplicationContext());


            // Recupere nickname e password salvata
            String nickname = sharedActivity.getNickname();
            String mail = sharedActivity.getEmail();
            Log.d("MainActivity", "Utente loggato: " + nickname);

            ConstraintLayout mainLayout = findViewById(R.id.main_layout);
            ConstraintLayout sidebar = findViewById(R.id.profile_sidebar);


            TextView profileButton = findViewById(R.id.profile_btn);
            TextView playButton = findViewById(R.id.play_btn);
            TextView rulesButton = findViewById(R.id.rules_btn);
            TextView settingsButton = findViewById(R.id.setting_btn);
            TextView exitButton = findViewById(R.id.exit_btn);
            TextView friendsListButton = findViewById(R.id.lista_amici_btn);
            TextView areaUtenteButton = findViewById(R.id.area_utente_btn);
            TextView logoutButton = findViewById(R.id.logout_btn);
            TextView usernameSidebar = findViewById(R.id.username_sdb);
            TextView richiesteAmiciziaBtn = findViewById(R.id.richiesta_amicizia_btn);

            profileButton.setText(nickname);
            usernameSidebar.setText(nickname);


            profileButton.setOnClickListener( v->{
                //request al server per Lista Amici storico partite
                playerAPI.doGetPlayerAreaInfo(getApplicationContext());
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
                Intent intent = new Intent(MainActivity.this, GiocaActivity.class);
                startActivity(intent);
            });

            exitButton.setOnClickListener(v->{
                new AlertDialog.Builder(this)
                        .setTitle("Conferma uscita")
                        .setMessage("Sei sicuro di voler uscire dall'app?")
                        .setPositiveButton("Sì", (dialog, which) ->{finishAffinity();System.exit(0);})
                        .setNegativeButton("No", null)
                        .show();
            });

            friendsListButton.setOnClickListener(v->{
                Intent intent = new Intent(getApplicationContext(), ListaAmiciActivity.class);
                startActivity(intent);
            });


            richiesteAmiciziaBtn.setOnClickListener(v->{
                Intent intent = new Intent(getApplicationContext(), ListaRichiesteAmiciActivity.class);
                startActivity(intent);
            });

            rulesButton.setOnClickListener(v->{
                Intent intent = new Intent(getApplicationContext(), RegoleRuoliActivity.class);
                startActivity(intent);
            });

            logoutButton.setOnClickListener(v->{
                playerAPI.doLogout(getApplicationContext(), sharedActivity);
            });

            areaUtenteButton.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), PlayerAreaActivity.class);
                startActivity(intent);
            });

            settingsButton.setOnClickListener(v->{
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            });
        }
}

    @Override
    protected void onResume() {
        super.onResume();
        TextView usernameSidebar = findViewById(R.id.username_sdb);
        TextView profileButton = findViewById(R.id.profile_btn);

        profileButton.setText(SharedActivity.getInstance(getApplicationContext()).getNickname());
        usernameSidebar.setText(SharedActivity.getInstance(getApplicationContext()).getNickname());
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy:sessione " + sharedActivity.getSessionId());
        if (sharedActivity != null) {
            sharedActivity.saveData();
        }
        super.onDestroy();
    }
}

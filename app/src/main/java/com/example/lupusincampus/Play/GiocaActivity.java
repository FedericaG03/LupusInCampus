package com.example.lupusincampus.Play;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.Amici.ListaAmiciActivity;
import com.example.lupusincampus.Amici.ListaRichiesteAmiciActivity;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.Play.GestioneLogicaPartita.CreateLobbyActivity;
import com.example.lupusincampus.Play.GestioneLogicaPartita.LobbyListActivity;
import com.example.lupusincampus.PlayerArea.PlayerAreaActivity;
import com.example.lupusincampus.ServerConnector;

import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

public class GiocaActivity extends BaseActivity {


    private static final String TAG = "GiocaActivity";
    private TextView inviteNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gioca_activity);
        SharedActivity sharedActivity = SharedActivity.getInstance(getApplicationContext());
        PlayerAPI playerAPI = new PlayerAPI();
        Log.d(TAG, "onCreate: Posso iniziare a giocare ");

        String nickname = sharedActivity.getNickname();
        RelativeLayout mainLayout = findViewById(R.id.main_layout_gioca);
        ConstraintLayout sidebar = findViewById(R.id.profile_sidebar);
        TextView profileButton = findViewById(R.id.profile_btn);
        TextView bckButton = findViewById(R.id.back_btn);
        TextView usernameSidebar = findViewById(R.id.username_sdb);
        TextView areaUtenteBtn = findViewById(R.id.area_utente_btn);
        TextView listaAmiciBtn = findViewById(R.id.lista_amici_btn);
        TextView richiesteAmiciBtn = findViewById(R.id.richiesta_amicizia_btn);
        TextView listaInvitiButton = findViewById(R.id.lista_inviti_btn);
        TextView logoutBtn = findViewById(R.id.logout_btn);
        TextView btnShowLobby = findViewById(R.id.btnShowLobby);
        TextView btnCreateGame = findViewById(R.id.btnCreateGame);

        profileButton.setText(nickname);
        usernameSidebar.setText(nickname);
        inviteNotification = findViewById(R.id.inviteNotification);

        profileButton.setOnClickListener(v->{
            if(profileButton.getVisibility() == View.VISIBLE) {
                playerAPI.doGetPlayerAreaInfo(getApplicationContext());
                profileButton.setVisibility(View.INVISIBLE);
                profileButton.invalidate();
                sidebar.setVisibility(View.VISIBLE);
                sidebar.invalidate();
            }
        });

        mainLayout.setOnClickListener(v->{
            if (sidebar.getVisibility() == View.VISIBLE) {
                sidebar.setVisibility(View.INVISIBLE);
                sidebar.invalidate();
                profileButton.setVisibility(View.VISIBLE);
                profileButton.invalidate();
            }
        });

        areaUtenteBtn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), PlayerAreaActivity.class);
            startActivity(intent);
        });

        listaAmiciBtn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), ListaAmiciActivity.class);
            startActivity(intent);
        });

        richiesteAmiciBtn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), ListaRichiesteAmiciActivity.class);
            startActivity(intent);
        });

        listaInvitiButton.setOnClickListener(v->{
            /*TODO*/
        });

        logoutBtn.setOnClickListener(v->{
            playerAPI.doLogout(getApplicationContext(), sharedActivity);
        });


        btnShowLobby.setOnClickListener(view -> {
            Intent intent = new Intent(GiocaActivity.this, LobbyListActivity.class);
            startActivity(intent);
            Log.d(TAG, "onCreate: vado al lobbyActivity");
        });


        btnCreateGame.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateLobbyActivity.class);
            startActivity(intent);
            Log.d(TAG, "onCreate: vado al lobbyActivity");
        });


        profileButton.setOnClickListener( v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.GONE);
                sidebar.setVisibility(View.VISIBLE);
            }
        });

        inviteNotification = findViewById(R.id.inviteNotification);
        // Recupera il numero di inviti dal server
        fetchInvitesFromServer();


        bckButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

    }

    /**
     * Gestione inviti
     */
    private void fetchInvitesFromServer() {

    }
    private void updateInviteNotification(int invites) {
        if (invites > 0) {
            String text = invites + " Amici Ti Hanno Invitato";
            inviteNotification.setText(text);
        } else {
            inviteNotification.setText("Nessun invito disponibile");
        }
    }

}




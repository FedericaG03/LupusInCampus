package com.example.lupusincampus.Play;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.Play.GestioneLogicaPartita.LobbyActivityWait;
import com.example.lupusincampus.Play.GestioneLogicaPartita.LobbyListActivity;
import com.example.lupusincampus.ServerConnector;

import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;
import com.example.lupusincampus.databinding.LobbyWaitingBinding;

public class WordActivity extends BaseActivity {


    private static final String TAG = "WordActivity";
    private TextView inviteNotification;
    private View bckButton;
    private SharedActivity sharedActivity;
    private TextView btnShowLobby;
    private TextView btnCreateGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mondo_activity);

        String nickname = sharedActivity.getNickname();
        ConstraintLayout sidebar = findViewById(R.id.profile_sidebar);
        TextView profileButton = findViewById(R.id.profile_btn);
        TextView bckButton = findViewById(R.id.back_btn);
        profileButton.setText(nickname);
        //Bottoni gioco mostra lobby
        btnShowLobby = findViewById(R.id.btnShowLobby);
        btnCreateGame = findViewById(R.id.btnCreateGame);

        inviteNotification = findViewById(R.id.inviteNotification);


        btnShowLobby.setOnClickListener(view -> {
            Intent intent = new Intent(WordActivity.this, LobbyListActivity.class);
            startActivity(intent);
            Log.d(TAG, "onCreate: vado al lobbyActivity");
        });


        btnCreateGame.setOnClickListener(view -> {
            //TODO: CREARE LA CLASSE CHE TI CREARE UNA LOBBY ( ANCHE L'XML)
            Intent intent = new Intent(this, LobbyActivityWait.class);
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
        ServerConnector connector = new ServerConnector();
        // Chiama la funzione fetchInvites
        /*connector.fetchInvites(this,new ServerConnector.FetchDataCallback() {
            @Override
            public void onSuccess(String jsonResponse) {
                try {
                    // Analizza la risposta JSON
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    int invites = jsonObject.getInt("invites");
                    //Aggiorna la UI con il numero di inviti
                    runOnUiThread(() -> updateInviteNotification(invites));
                } catch (Exception e) {
                    Log.d(TAG, "Errore nel parsing della risposta JSON: " + e.getMessage());
                }
            }
            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(WordActivity.this, "Errore durante la connessione: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onServerOffline(Exception e) {
                Toast.makeText(WordActivity.this, "Errore durante la connessione: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });*/
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




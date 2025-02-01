package com.example.lupusincampus.Play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.ServerConnector;

import org.json.JSONObject;

import com.example.lupusincampus.R;

public class WordActivity extends AppCompatActivity {


    private static final String TAG = "WordActivity";
    private TextView inviteNotification;
    private View bckButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mondo_activity);


        inviteNotification = findViewById(R.id.inviteNotification);
        bckButton = findViewById(R.id.back_btn);
        // Recupera il numero di inviti dal server
        fetchInvitesFromServer();


        bckButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

    }

    private void fetchInvitesFromServer() {
        ServerConnector connector = new ServerConnector();
        // Chiama la funzione fetchInvites
        connector.fetchInvites(new ServerConnector.FetchDataCallback() {
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
        });
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




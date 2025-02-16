package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.API.websocket.Subscriber;
import com.example.lupusincampus.API.websocket.WebSocketObserver;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.Model.Ruolo;
import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PartitaActivity extends BaseActivity implements Subscriber {
    private static final String TAG = "PartitaActivity";
    private Button btnExit, btnPlayers;
    private ImageView micButton, playerAvatar;
    private TextView storyText, playerName, roleDescription;
    private SharedActivity sharedActivity;
    private ServerConnector serverConnector;
    private List<String> storyLines = new ArrayList<>();
    private int currentLineIndex = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partita_activity);

        WebSocketObserver.getInstance().subscribe(WebSocketObserver.EventType.ROLE, this);

        sharedActivity = SharedActivity.getInstance(this);
        serverConnector = new ServerConnector();
        String nickname = sharedActivity.getNickname();
        TextView profileButton = findViewById(R.id.profile_btn);
        profileButton.setText(nickname);

        btnExit = findViewById(R.id.btn_exit);
        btnPlayers = findViewById(R.id.btn_players);

        playerAvatar = findViewById(R.id.player_avatar);
        storyText = findViewById(R.id.story_text);
        playerName = findViewById(R.id.player_name);


        // Carica una storia  all'inizio
        loadStory();
        //Mostra frasi
        showNextStoryLine();

        // Richiesta del ruolo dal server
       // fetchRoleFromServer();

        // Listener per uscire dalla partita
        btnExit.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });


        // Listener per il microfono
        micButton.setOnClickListener(v -> Log.d(TAG, "Microfono premuto"));

        // Listener per mostrare i giocatori
        btnPlayers.setOnClickListener(v -> Log.d(TAG, "Mostra lista giocatori"));
    }

    /**
     *Gestione storia
     */
    private void loadStory() {
        File file = new File(getApplicationContext().getExternalFilesDir(null), "IlCampusMaledetto.txt");
        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i(TAG, line);
            }
            reader.close();
            inputStream.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    private void showNextStoryLine() {
        if (currentLineIndex < storyLines.size()) {
            storyText.setText(storyLines.get(currentLineIndex)); // Mostra la frase corrente
            currentLineIndex++;  // Incrementa l'indice per la frase successiva

            // Mostra la prossima frase dopo 60 secondi (60000 millisecondi)
            handler.postDelayed(this::showNextStoryLine, 60000);
        } else {
            // Quando la storia è finita, puoi decidere cosa fare
            Log.d(TAG, "Storia finita.");
            // Se vuoi, puoi caricare una nuova storia o fare altre azioni
        }
    }



    // Metodo per ottenere il ruolo dal server
 /*   private void fetchRoleFromServer() {
        serverConnector.fetchRole(this,new ServerConnector.FetchDataCallback() {
            @Override
            public void onSuccess(String jsonResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONObject roleObject = jsonObject.getJSONObject("role");  // Estrai l'oggetto "role"

                    // Estrai i dettagli del ruolo
                    String roleName = roleObject.getString("name");
                    String roleDescriptionText = roleObject.getString("description");
                    int roleImageResId = getResources().getIdentifier(
                            roleObject.getString("imageResId"), "drawable", getPackageName());

                    // Crea un oggetto Ruolo
                    Ruolo ruolo = new Ruolo(roleName, roleDescriptionText, roleImageResId);
                    playerAvatar.setImageResource(ruolo.getImageResId());  // Imposta l'immagine dell'avatar
                    playerName.setText(ruolo.getNome());  // Imposta il nome del ruolo
                    roleDescription.setText(ruolo.getDescrizione());  // Imposta la descrizione del ruolo

                } catch (JSONException e) {
                    Log.e(TAG, "Errore nell'analisi del ruolo: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Errore nel recupero del ruolo: " + e.getMessage(), e);
            }

            @Override
            public void onServerOffline(Exception e) {
                Log.e(TAG, "Errore connessione server: " + e.getMessage(), e);
            }

        });
    }*/

    @Override
    public void update(JSONObject data, WebSocketObserver.EventType eventType) {

        if (eventType == WebSocketObserver.EventType.ROLE){
            try {
                Log.d(TAG, "update: ricevuto ruolo " + data.getString("data"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }
}



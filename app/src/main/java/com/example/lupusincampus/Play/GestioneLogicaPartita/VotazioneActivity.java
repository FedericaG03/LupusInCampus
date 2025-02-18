package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;


import java.util.List;

public class VotazioneActivity extends BaseActivity {
    String votedPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.votazione_activity);

        TextView descrizioneTurno = findViewById(R.id.descrizione_turno);
        TextView confermaBtn = findViewById(R.id.conferma_voto_btn);
        TextView skipBtn = findViewById(R.id.skip_button);
        RecyclerView listaPlayerVotabili = findViewById(R.id.lista_player_votabili);

        String gamePhase = this.getIntent().getStringExtra("game_phase");
        String lobbyCode = this.getIntent().getStringExtra("lobby_code");


        String testoDescrizione;

        switch (gamePhase){
            case "werewolves" : testoDescrizione = "Fuori corso è il vostro turno, decidete chi eliminare"; break;
            case "bodyguard" : testoDescrizione = "Rettore è il tuo turno decidi chi salvare"; break;
            case "seer" : testoDescrizione = "Ricercaotre è il tuo turno decici chi vuoi spezionare"; break;
            case "discussion": testoDescrizione = "E' il momento della discussione, decidete chi eliminare"; break;
            default: testoDescrizione = "fase di gioco";
        }

        descrizioneTurno.setText(testoDescrizione);

        LobbyDatabaseHelper db = new LobbyDatabaseHelper(getApplicationContext());
        List<String> playersName = db.getPlayesByLobbyID(Integer.parseInt(lobbyCode));

        if (gamePhase.equals("discussion")){
            skipBtn.setVisibility(View.VISIBLE);
            skipBtn.invalidate();
        } else{
            skipBtn.setVisibility(View.INVISIBLE);
            skipBtn.invalidate();
        }

        VotazioneAdapter adapter = new VotazioneAdapter(playersName, playerName -> {
            Log.d("VotazioneActivity" , "Voted player = " + votedPlayer);
            votedPlayer = playerName;
        });

        listaPlayerVotabili.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        listaPlayerVotabili.setAdapter(adapter);

        confermaBtn.setOnClickListener(v->{
            Intent resultIntent = new Intent();
            resultIntent.putExtra("voted_player", votedPlayer);
            resultIntent.putExtra("game_phase", gamePhase);
            setResult(PartitaActivity.RESULT_OK, resultIntent);
            finish();

        });




    }
}
package com.example.lupusincampus.Regole;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.Amici.ListaAmiciActivity;
import com.example.lupusincampus.Login.LoginActivity;
import com.example.lupusincampus.Model.Ruolo;
import com.example.lupusincampus.R;
import com.example.lupusincampus.Server.PlayerAPI;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import java.util.ArrayList;
import java.util.List;

public class RuoliActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruoli_activity);

        SharedActivity sharedActivity = SharedActivity.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.lista_ruoli);
        View backButton = findViewById(R.id.back_btn);
        TextView profileButton = findViewById(R.id.profile_btn);
        TextView friensListButton = findViewById(R.id.lista_amici_btn);
        TextView logoutButton = findViewById(R.id.logout_btn);
        ConstraintLayout sidebarGeneral = findViewById(R.id.profile_sidebar);
        ConstraintLayout mainLayout = findViewById(R.id.ruoli_layout);
        View sidebarRuolo = findViewById(R.id.profile_sidebar_roles);


        initializeRuoli();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Ruolo> ruoli = initializeRuoli();
        RuoliAdapter adapter = new RuoliAdapter(ruoli);
        recyclerView.setAdapter(adapter);

        profileButton.setOnClickListener( v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.GONE);
                sidebarRuolo.bringToFront();
                sidebarGeneral.setVisibility(View.VISIBLE);
            }
        });

        mainLayout.setOnClickListener(v->{
            if(sidebarGeneral.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.VISIBLE);
                sidebarGeneral.setVisibility(View.GONE);
            }
        });

        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        friensListButton.setOnClickListener(v->{
            sidebarGeneral.setVisibility(View.GONE);
            Intent intent = new Intent(this, ListaAmiciActivity.class);
        });

        logoutButton.setOnClickListener(v->{
            PlayerAPI playerAPI = new PlayerAPI();
            playerAPI.doLogout(getApplicationContext(), sharedActivity);
        });
    }

    private List<Ruolo> initializeRuoli(){
        List<Ruolo> ruoli = new ArrayList<>();
        ruoli.add(new Ruolo("Studente fuori corso", "Il Studente Fuori Corso ha perso ogni speranza di laurearsi in tempo e ora cova rancore verso il sistema accademico. Ogni notte colpisce nell’ombra, cercando di trascinare con sé altri studenti nella sua eterna sessione d’esami infinita.", R.drawable.logo_studente_fuori_corso));
        ruoli.add(new Ruolo("Rettore", "Il Rettore è il pilastro dell’università, impegnato a difendere gli studenti più meritevoli. Ogni notte può proteggere uno di loro dagli attacchi, garantendogli un futuro accademico più sicuro. Tuttavia, non può proteggere lo stesso studente per due notti consecutive.", R.drawable.logo_rettore));
        ruoli.add(new Ruolo("Laureando", "Il Laureando ha faticato anni per arrivare alla fine del percorso universitario. Se viene eliminato, in un ultimo atto di rivalsa, può trascinare con sé un altro giocatore, facendo valere la sua esperienza accumulata con mille esami.", R.drawable.logo_laureando));
        ruoli.add(new Ruolo("Ricercatore", "Esperto nelle arti dell'analisi e della deduzione, il Ricercatore Universitario può scoprire ogni notte il vero ruolo di un altro giocatore. Grazie ai suoi studi, cerca di smascherare le minacce nascoste nel campus, ma deve stare attento a non attirare troppe attenzioni su di sé.", R.drawable.logo_ricercatore));
        ruoli.add(new Ruolo("Studente in corso", "Lo Studente è il cuore pulsante dell’università: segue le lezioni, affronta gli esami e cerca di sopravvivere fino alla laurea. Non ha abilità speciali, ma con il suo intuito e le sue votazioni può influenzare le sorti del campus. Anche se può sembrare insignificante, ogni studente ha il potere di cambiare il destino dell’università!", R.drawable.logo_studente_in_corso));
        return ruoli;
    }
}

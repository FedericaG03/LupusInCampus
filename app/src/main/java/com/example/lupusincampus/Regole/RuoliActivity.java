package com.example.lupusincampus.Regole;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.Amici.ListaAmiciActivity;
import com.example.lupusincampus.Amici.ListaRichiesteAmiciActivity;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.PlayerArea.PlayerAreaActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.SharedActivity;
import com.example.lupusincampus.Model.Ruolo;

import java.util.ArrayList;
import java.util.List;

public class RuoliActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruoli_activity);
        SharedActivity sharedActivity = SharedActivity.getInstance(this);
        PlayerAPI playerAPI = new PlayerAPI();

        String nickname = sharedActivity.getNickname();

        ConstraintLayout mainLayout = findViewById(R.id.ruoli_layout);
        ConstraintLayout sidebarGeneral = findViewById(R.id.profile_sidebar);
        View sidebarRuolo = findViewById(R.id.profile_sidebar_roles);
        TextView profileButton = findViewById(R.id.profile_btn);
        TextView usernameSidebar = findViewById(R.id.username_sdb);
        TextView areaUtenteBtn = findViewById(R.id.area_utente_btn);
        TextView listaAmiciBtn = findViewById(R.id.lista_amici_btn);
        TextView richiesteAmiciBtn = findViewById(R.id.richiesta_amicizia_btn);
        TextView listaInvitiBtn = findViewById(R.id.lista_inviti_btn);
        TextView logoutButton = findViewById(R.id.logout_btn);
        RecyclerView recyclerView = findViewById(R.id.lista_ruoli);
        TextView backButton = findViewById(R.id.back_btn);

        profileButton.setText(nickname);
        usernameSidebar.setText(nickname);
        initializeRuoli();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<Ruolo> ruoli = initializeRuoli();
        RuoliAdapter adapter = new RuoliAdapter(ruoli);
        recyclerView.setAdapter(adapter);

        profileButton.setOnClickListener( v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.INVISIBLE);
                profileButton.invalidate();
                sidebarRuolo.bringToFront();
                sidebarGeneral.setVisibility(View.VISIBLE);
                sidebarGeneral.invalidate();
            }
        });

        mainLayout.setOnClickListener(v->{
            if(sidebarGeneral.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.VISIBLE);
                profileButton.invalidate();
                sidebarGeneral.setVisibility(View.INVISIBLE);
                sidebarGeneral.invalidate();
            }
        });

        areaUtenteBtn.setOnClickListener(v->{
            playerAPI.doGetPlayerAreaInfo(getApplicationContext());
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

        listaInvitiBtn.setOnClickListener(v->{
            /*TODO*/
        });

        logoutButton.setOnClickListener(v->
                playerAPI.doLogout(getApplicationContext(), sharedActivity));

        backButton.setOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed());

    }

    private List<Ruolo> initializeRuoli(){
        List<Ruolo> ruoli = new ArrayList<>();
        ruoli.add(new Ruolo("Studente fuori corso", "Lo Studente Fuori Corso ha perso ogni speranza di laurearsi in tempo e ora cova rancore verso il sistema accademico. Ogni notte colpisce nell’ombra, cercando di trascinare con sé altri studenti nella sua eterna sessione d’esami infinita.", R.drawable.logo_studente_fuori_corso));
        ruoli.add(new Ruolo("Rettore", "Il Rettore è il pilastro dell’università, impegnato a difendere gli studenti più meritevoli. Ogni notte può proteggere uno di loro dagli attacchi, garantendogli un futuro accademico più sicuro. Tuttavia, non può proteggere lo stesso studente per due notti consecutive.", R.drawable.logo_rettore));
        ruoli.add(new Ruolo("Laureando", "Il Laureando ha faticato anni per arrivare alla fine del percorso universitario. Se viene eliminato, in un ultimo atto di rivalsa, può trascinare con sé un altro giocatore, facendo valere la sua esperienza accumulata con mille esami.", R.drawable.logo_laureando));
        ruoli.add(new Ruolo("Ricercatore", "Esperto nelle arti dell'analisi e della deduzione, il Ricercatore Universitario può scoprire ogni notte il vero ruolo di un altro giocatore. Grazie ai suoi studi, cerca di smascherare le minacce nascoste nel campus, ma deve stare attento a non attirare troppe attenzioni su di sé.", R.drawable.logo_ricercatore));
        ruoli.add(new Ruolo("Studente in corso", "Lo Studente è il cuore pulsante dell’università: segue le lezioni, affronta gli esami e cerca di sopravvivere fino alla laurea. Non ha abilità speciali, ma con il suo intuito e le sue votazioni può influenzare le sorti del campus. Anche se può sembrare insignificante, ogni studente ha il potere di cambiare il destino dell’università!", R.drawable.logo_studente_in_corso));
        return ruoli;
    }

}

package com.example.lupusincampus.Amici;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.API.FriendAPI;
import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.PlayerArea.PlayerAreaActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListaAmiciActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.lista_amici_activity);

        SharedActivity sharedActivity = SharedActivity.getInstance(getApplicationContext());
        PlayerAPI playerAPI = new PlayerAPI();
        String nickname = sharedActivity.getNickname();

        ConstraintLayout mainLayout = findViewById(R.id.lista_amici_layout);
        ConstraintLayout sidebarGeneral = findViewById(R.id.profile_sidebar);
        View sidebarListaAmici = findViewById(R.id.profile_sidebar_lista_amici);
        TextView profileButton = findViewById(R.id.profile_btn);
        TextView usernameSidebar = findViewById(R.id.username_sdb);
        TextView areaUtenteBtn = findViewById(R.id.area_utente_btn);
        TextView listaAmiciBtn = findViewById(R.id.lista_amici_btn);
        TextView richiesteAmiciBtn = findViewById(R.id.richiesta_amicizia_btn);
        TextView listaInvitiBtn = findViewById(R.id.lista_inviti_btn);
        TextView logoutButton = findViewById(R.id.logout_btn);
        TextView backButton = findViewById(R.id.back_btn);

        profileButton.setText(nickname);
        usernameSidebar.setText(nickname);

        recyclerView = findViewById(R.id.recycler_view_amici);
        ConstraintLayout addFriendButton = findViewById(R.id.aggiungi_amico_btn);
        progressBar = findViewById(R.id.progress_bar_lista_amici);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        profileButton.setOnClickListener( v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.INVISIBLE);
                profileButton.invalidate();
                sidebarListaAmici.bringToFront();
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

        addFriendButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AggiungiAmicoActivity.class);
            startActivity(intent);
        });

        // Carica la lista amici
        loadFriendList();
    }

    private void loadFriendList() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.invalidate();
        progressBar.invalidate();

        executorService.execute(() -> {
            List<Player> friendList = SharedActivity.getInstance(getApplicationContext()).getFriendList();

            for (int i = 0; i <= 100; i += 20) {  // Simulazione di avanzamento progressivo
                int progress = i;
                mainHandler.post(() -> progressBar.setProgress(progress));
                try {
                    Thread.sleep(250); // Simula il caricamento di una parte dei dati
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Aggiorna la UI nel thread principale
            mainHandler.post(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.invalidate();
                recyclerView.invalidate();

                // Configura il RecyclerView
                ListaAmiciAdapter listaAmiciAdapter = new ListaAmiciAdapter(friendList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(listaAmiciAdapter);
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}

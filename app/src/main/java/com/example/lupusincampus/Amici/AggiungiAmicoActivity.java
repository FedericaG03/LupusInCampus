package com.example.lupusincampus.Amici;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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

public class AggiungiAmicoActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_amico_activity);

        SharedActivity sharedActivity = SharedActivity.getInstance(getApplicationContext());
        PlayerAPI playerAPI = new PlayerAPI();
        String nickname = sharedActivity.getNickname();

        ConstraintLayout mainLayout = findViewById(R.id.main_layout_aggiugni_amico);
        ConstraintLayout sidebarAggiugniAmico = findViewById(R.id.sidebar_aggiungi_amico);
        ConstraintLayout sidebarGeneral = findViewById(R.id.profile_sidebar);
        TextView profileButton = findViewById(R.id.profile_btn);
        TextView usernameSidebar = findViewById(R.id.username_sdb);
        TextView areaUtenteBtn = findViewById(R.id.area_utente_btn);
        TextView listaAmiciBtn = findViewById(R.id.lista_amici_btn);
        TextView richiesteAmiciBtn = findViewById(R.id.richiesta_amicizia_btn);
        TextView logoutButton = findViewById(R.id.logout_btn);

        EditText searchBar = findViewById(R.id.aggiungi_amico_search_bar);
        TextView backButton = findViewById(R.id.back_btn);
        ImageView searchButton = findViewById(R.id.search_icon_btn);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_aggiungi_amico);

        profileButton.setText(nickname);
        usernameSidebar.setText(nickname);

        profileButton.setOnClickListener( v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.INVISIBLE);
                profileButton.invalidate();
                sidebarAggiugniAmico.bringToFront();
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

        logoutButton.setOnClickListener(v->
                playerAPI.doLogout(getApplicationContext(), sharedActivity));


        backButton.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });


        FriendAPI friendAPI = new FriendAPI();

        searchButton.setOnClickListener(v->{
            friendAPI.doSearchPlayer(getApplicationContext(), searchBar.getText().toString());
            List<Player> searchResult = sharedActivity.getSearchResult();

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            AggiungiAmicoAdapter.OnFriendClickListener listener = friend -> {
                Player clickedPlayer = new Player();
                for (Player p : searchResult) {
                    if (p.getNickname().equals(friend.getNickname())) {
                        clickedPlayer = p;
                    }
                }
                friendAPI.doSendFriendRequest(getApplicationContext(), clickedPlayer.getId());
            };
            AggiungiAmicoAdapter adapter = new AggiungiAmicoAdapter(searchResult, listener);
            recyclerView.setAdapter(adapter);
        });




    }
}

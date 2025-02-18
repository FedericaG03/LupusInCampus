package com.example.lupusincampus.Regole;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lupusincampus.Amici.ListaAmiciActivity;
import com.example.lupusincampus.Amici.ListaRichiesteAmiciActivity;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.PlayerArea.PlayerAreaActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.SharedActivity;

public class RegoleActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regole_activity);
        SharedActivity sharedActivity = SharedActivity.getInstance(this);
        PlayerAPI playerAPI = new PlayerAPI();

        String nickname = sharedActivity.getNickname();

        ConstraintLayout mainLayout = findViewById(R.id.regole_layout);
        ConstraintLayout sidebarGeneral = findViewById(R.id.profile_sidebar);
        TextView profileButton = findViewById(R.id.profile_btn);
        TextView nicknameSidebar = findViewById(R.id.username_sdb);
        TextView areaUtenteBtn = findViewById(R.id.area_utente_btn);
        TextView listaAmiciBtn = findViewById(R.id.lista_amici_btn);
        TextView richiesteAmiciBtn = findViewById(R.id.richiesta_amicizia_btn);
        TextView logoutBtn = findViewById(R.id.logout_btn);
        TextView backBtn = findViewById(R.id.back_btn);
        View sidebarRegole = findViewById(R.id.profile_sidebar_rules);

        profileButton.setText(nickname);
        nicknameSidebar.setText(nickname);

        profileButton.setOnClickListener(v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.INVISIBLE);
                profileButton.invalidate();
                sidebarRegole.bringToFront();
                sidebarGeneral.setVisibility(View.VISIBLE);
                sidebarGeneral.invalidate();
            }
        });

        mainLayout.setOnClickListener(v->{
            if(sidebarGeneral.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.VISIBLE);
                profileButton.invalidate();
                sidebarGeneral.setVisibility(View.GONE);
                sidebarRegole.invalidate();
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

        logoutBtn.setOnClickListener(v->{
            playerAPI.doLogout(getApplicationContext(), sharedActivity);
        });

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

    }
}

package com.example.lupusincampus.Regole;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lupusincampus.R;
import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.SharedActivity;

public class RegoleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regole_activity);

        SharedActivity sharedActivity = SharedActivity.getInstance(this);

        TextView profileButton = findViewById(R.id.profile_btn);
        TextView backButton = findViewById(R.id.back_btn);
        TextView friendsListButton = findViewById(R.id.lista_amici_btn);
        TextView logoutButton = findViewById(R.id.logout_btn);
        ConstraintLayout sidebarGeneral = findViewById(R.id.profile_sidebar);
        ConstraintLayout mainLayout = findViewById(R.id.regole_layout);
        View sidebarRegole = findViewById(R.id.profile_sidebar_rules);


        profileButton.setOnClickListener(v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.GONE);
                sidebarRegole.bringToFront();
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

        logoutButton.setOnClickListener(v->{
            PlayerAPI playerAPI = new PlayerAPI();
            playerAPI.doLogout(getApplicationContext(), sharedActivity);
        });
    }
}

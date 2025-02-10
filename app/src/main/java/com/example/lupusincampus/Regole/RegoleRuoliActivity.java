package com.example.lupusincampus.Regole;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lupusincampus.Amici.ListaAmiciActivity;
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.Play.LocalActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.API.PlayerAPI;
import com.example.lupusincampus.SharedActivity;

public class RegoleRuoliActivity extends BaseActivity {

    private static final String TAG = "PlayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regole_ruoli_activity);
        SharedActivity sharedActivity = SharedActivity.getInstance(this);


        RelativeLayout mainLayout = findViewById(R.id.regole_ruoli_layout);
        ConstraintLayout sidebarGeneral = findViewById(R.id.profile_sidebar);
        View sidebarRegoleRuolo = findViewById(R.id.profile_sidebar_regole_ruoli);
        TextView backButton = findViewById(R.id.back_btn);
        TextView profileButton = findViewById(R.id.profile_btn);
        TextView friendsListButton = findViewById(R.id.lista_amici_btn);
        TextView logoutButton = findViewById(R.id.logout_btn);
        ImageButton btnRules = findViewById(R.id.btn_rules);
        ImageButton btnRoles = findViewById(R.id.btn_roles);

        profileButton.setOnClickListener( v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.GONE);
                sidebarRegoleRuolo.bringToFront();
                sidebarGeneral.setVisibility(View.VISIBLE);

            }
        });

        mainLayout.setOnClickListener(v->{
            if(sidebarGeneral.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.VISIBLE);
                sidebarGeneral.setVisibility(View.GONE);
            }
        });

        friendsListButton.setOnClickListener(v->{
            sidebarGeneral.setVisibility(View.GONE);
            Intent intent = new Intent(this, ListaAmiciActivity.class);
            startActivity(intent);
        });

        btnRules.setOnClickListener(v -> {
            Intent intent = new Intent(RegoleRuoliActivity.this, RegoleActivity.class);
            startActivity(intent);
        });

        btnRoles.setOnClickListener(v -> {
            Intent intent = new Intent(RegoleRuoliActivity.this, RuoliActivity.class);
            startActivity(intent);
            Log.d(TAG, "onCreate: vado al wordActivity");
        });

        backButton.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        logoutButton.setOnClickListener(v->{
            PlayerAPI playerAPI = new PlayerAPI();
            playerAPI.doLogout(getApplicationContext(), sharedActivity);
        });
    }
}
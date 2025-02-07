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

import com.example.lupusincampus.Login.LoginActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;

public class RegoleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regole_activity);

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
            new ServerConnector().logoutReqeust(this,new ServerConnector.FetchDataCallback() {
                @Override
                public void onSuccess(String jsonResponse) {
                    Log.d("MainActivity", "Effettuato logout con successo");
                    Toast.makeText(getApplicationContext(), "Effettuato logout con successo!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("RegoleRuoliActivity", "Impossibile effettuare logout!");
                    Toast.makeText(getApplicationContext(), "Impossibile effettuare logout!" , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onServerOffline(Exception e) {
                    Log.e("RegoleRuoliActivity", "Impossibile effettuare logout!");
                    Toast.makeText(getApplicationContext(), "Errore server" , Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

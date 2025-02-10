package com.example.lupusincampus.Amici;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;
import com.fasterxml.jackson.databind.ser.Serializers;

public class ListaRichiesteAmici extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.lista_amici_activity);

        View bckButton = findViewById(R.id.back_btn);

        ConstraintLayout aggiungiAmicoButton = findViewById(R.id.aggiungi_amico_btn);
        aggiungiAmicoButton.setVisibility(View.GONE); // Nasconde il bottone

        RecyclerView recyclerView = findViewById(R.id.recycler_view_amici);

        bckButton.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });


        ListaAmiciAdapter listaAmiciAdapter = new ListaAmiciAdapter(SharedActivity.getInstance(getApplicationContext()).getPlayerRequestList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(listaAmiciAdapter);

    }
}






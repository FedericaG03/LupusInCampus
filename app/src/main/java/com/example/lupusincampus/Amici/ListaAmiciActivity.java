package com.example.lupusincampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListaAmiciActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.lista_amici_activity);

        View bckButton = findViewById(R.id.back_btn);

        ConstraintLayout aggiungiAmicoButton = findViewById(R.id.aggiungi_amico_btn);

        List<String> listaAmici = new ArrayList<>();
        ListaAmiciAdapter listaAmiciAdapter = new ListaAmiciAdapter(listaAmici);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_amici);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaAmiciAdapter);

        bckButton.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });

        /*TODO prendere lista amici dal database*/
        listaAmici.add("chrizzo");
        listaAmici.add("Piergiangelo");
        listaAmici.add("Pippo");

        aggiungiAmicoButton.setOnClickListener(v->{
            listaAmiciAdapter.addFriend("Gianni");
        });
    }
}

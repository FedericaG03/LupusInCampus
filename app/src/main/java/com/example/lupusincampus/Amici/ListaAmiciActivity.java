package com.example.lupusincampus.Amici;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
*/
import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

public class ListaAmiciActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.lista_amici_activity);

        View bckButton = findViewById(R.id.back_btn);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_amici);
        ConstraintLayout addFriendButton = findViewById(R.id.aggiungi_amico_btn);


        bckButton.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

        addFriendButton.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), AggiungiAmicoActivity.class);
            startActivity(intent);
        });



        ListaAmiciAdapter listaAmiciAdapter = new ListaAmiciAdapter(SharedActivity.getInstance(getApplicationContext()).getPlayerList());
        Log.d("ListaAmiciActivity", "firenlist: " + SharedActivity.getInstance(getApplicationContext()).getPlayerList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(listaAmiciAdapter);



    }
}

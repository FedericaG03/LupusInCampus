package com.example.lupusincampus.Amici;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
*/
import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.R;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ListaAmiciActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.lista_amici_activity);

        View bckButton = findViewById(R.id.back_btn);

        ConstraintLayout aggiungiAmicoButton = findViewById(R.id.aggiungi_amico_btn);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_amici);


        /*bckButton.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

        ServerConnector serverConnector = new ServerConnector();
        String nickname = SharedActivity.getInstance(this).getNickname();
        serverConnector.fetchDataForFriendList(getApplicationContext(),nickname, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                try{
                    String jsonArrayString = jsonResponse.getJSONArray("body").toString();
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Player> listaAmici = objectMapper.readValue(jsonArrayString, new TypeReference<List<Player>>() {});
                    ListaAmiciAdapter listaAmiciAdapter = new ListaAmiciAdapter(listaAmici);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(listaAmiciAdapter);
                } catch (JSONException e) {
                    this.onError(e.toString());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(String jsonResponse) {
                Log.e("LoginActivity", "Errore caricamento lista amici");
                Toast.makeText(ListaAmiciActivity.this, "Errore di nel caricamento lista amici!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onServerError(Exception e) {
                Log.e("LoginActivity", "Errore caricamento lista amici", e);
                Toast.makeText(ListaAmiciActivity.this, "Errore di nel caricamento lista amici!", Toast.LENGTH_SHORT).show();
            }
        });

        aggiungiAmicoButton.setOnClickListener(v->{
        //    Intent intent = new Intent(this, AggiungiAmicoActivity.class);

        });*/
    }
}

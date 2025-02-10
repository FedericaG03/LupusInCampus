package com.example.lupusincampus.PlayerArea;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;
import com.example.lupusincampus.SharedActivity;

public class HistoryGames extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.history_games_activity);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_games);



        HistoryGamesAdapter historyGamesAdapter = new HistoryGamesAdapter(SharedActivity.getInstance(getApplicationContext()).getGameList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(historyGamesAdapter);
    }
}
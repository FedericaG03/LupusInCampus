package com.example.lupusincampus.Play;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.lupusincampus.R;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mondo_locale_activity);
        Log.d("PlayActivity", "Sono alla playactivity");

        View bckButton = findViewById(R.id.back_btn);
        ImageButton imageButtonLocal = findViewById(R.id.btn_local);
        ImageButton imageButtonWord = findViewById(R.id.btn_word);


        imageButtonLocal.setOnClickListener(v -> {
            Intent intent = new Intent(PlayActivity.this, LocalActivity.class);
            startActivity(intent);
        });

        imageButtonWord.setOnClickListener(v -> {
            Intent intent = new Intent(PlayActivity.this, WordActivity.class);
            startActivity(intent);
        });
    }
}
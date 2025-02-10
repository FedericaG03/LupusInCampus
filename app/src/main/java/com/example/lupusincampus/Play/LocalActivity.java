package com.example.lupusincampus.Play;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.R;

public class LocalActivity extends BaseActivity {
    View bckButton;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        bckButton=findViewById(R.id.back_btn);




        bckButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });
    }

}


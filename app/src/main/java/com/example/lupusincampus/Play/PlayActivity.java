package com.example.lupusincampus.Play;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lupusincampus.R;

public class PlayActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mondo_locale_activity);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        LocalActivity localFragment = new LocalActivity();
        WordActivity wordActivity = new WordActivity();

        ft.replace(R.id.fragment_container, localFragment).commit();
        ft.replace(R.id.fragment_container, wordActivity).commit();
    }

}

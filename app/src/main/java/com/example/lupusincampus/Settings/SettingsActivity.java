package com.example.lupusincampus.Settings;

import android.content.Intent;
import android.content.SharedPreferences;;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lupusincampus.BaseActivity;
import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.R;
import com.google.android.material.slider.Slider;

import java.util.Locale;

public class SettingsActivity extends BaseActivity {
    private Spinner languageSpinner;
    private Slider musicSlider;
    private Slider effectsSlider;
    private TextView backButton;
    private TextView profileButton;
    private View sidebar;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "GameSettings";
    private static final String PREF_LANGUAGE = "language";
    private static final String PREF_MUSIC_VOLUME = "musicVolume";
    private static final String PREF_EFFECTS_VOLUME = "effectsVolume";
    private final String[] languageCodes = {"it", "en", "fr", "es", "de"};
    private boolean languageChanged = false;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize views
        initializeViews();

        // Load saved settings
        loadSettings();

        // Set up listeners
        setupListeners();
    }

    private void initializeViews() {
        languageSpinner = findViewById(R.id.spinner_lingua);
        musicSlider = findViewById(R.id.slider_musica);
        effectsSlider = findViewById(R.id.slider_effetti);
        backButton = findViewById(R.id.back_btn);
        sidebar = findViewById(R.id.profile_sidebar_settings);
        profileButton = findViewById(R.id.profile_btn);
        mainLayout = findViewById(R.id.setting_layout);

        // Set up language spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.languages_array, R.layout.spinner_lingua_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
    }

    private void loadSettings() {
        // Load language setting
        String savedLanguage = sharedPreferences.getString(PREF_LANGUAGE, "it");
        int languagePosition = getLanguagePosition(savedLanguage);
        languageSpinner.setSelection(languagePosition);

        // Load volume settings
        float musicVolume = sharedPreferences.getFloat(PREF_MUSIC_VOLUME, 100f);
        float effectsVolume = sharedPreferences.getFloat(PREF_EFFECTS_VOLUME, 100f);

        musicSlider.setValue(musicVolume);
        effectsSlider.setValue(effectsVolume);
    }

    private void setupListeners() {
        // Language spinner listener
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = getLanguageCode(position);

                if (!selectedLanguage.equals(sharedPreferences.getString(PREF_LANGUAGE, "it"))) {
                    languageChanged = true;  // Set flag to true when language is changed
                    saveLanguageSetting(selectedLanguage);
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        // Music volume slider listener
        musicSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                saveMusicVolumeSetting(value);
                updateMusicVolume(value);
            }
        });

        // Effects volume slider listener
        effectsSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                saveEffectsVolumeSetting(value);
                updateEffectsVolume(value);
            }
        });

        backButton.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v->{
            if(profileButton.getVisibility() == View.VISIBLE){
                profileButton.setVisibility(View.GONE);
                sidebar.setVisibility(View.VISIBLE);
            }
        });

        mainLayout.setOnClickListener(v->{
        if(sidebar.getVisibility() == View.VISIBLE){
            profileButton.setVisibility(View.VISIBLE);
            sidebar.setVisibility(View.GONE);
        }
    });
    }

    private void saveLanguageSetting(String languageCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LANGUAGE, languageCode);
        editor.apply();
    }

    private void saveMusicVolumeSetting(float volume) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(PREF_MUSIC_VOLUME, volume);
        editor.apply();
    }

    private void saveEffectsVolumeSetting(float volume) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(PREF_EFFECTS_VOLUME, volume);
        editor.apply();
    }

    private void updateMusicVolume(float volume) {
        // Implement your music volume update logic here
        // This might involve calling your game's audio manager
        // Example: AudioManager.setMusicVolume(volume / 100f);
    }

    private void updateEffectsVolume(float volume) {
        // Implement your sound effects volume update logic here
        // This might involve calling your game's audio manager
        // Example: AudioManager.setEffectsVolume(volume / 100f);
    }

    private String getLanguageCode(int position) {
        return (position >= 0 && position < languageCodes.length) ? languageCodes[position] : "it";
    }

    private int getLanguagePosition(String languageCode) {
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(languageCode)) return i;
        }
        return 0;
    }
}
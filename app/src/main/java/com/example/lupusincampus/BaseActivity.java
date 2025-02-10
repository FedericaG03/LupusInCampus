package com.example.lupusincampus;

import android.content.Context;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        // Ottieni la lingua salvata nelle preferenze
        String savedLanguage = newBase.getSharedPreferences("GameSettings", MODE_PRIVATE)
                .getString("language", "en");

        // Applica la lingua
        Locale locale = new Locale(savedLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);

        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }
}

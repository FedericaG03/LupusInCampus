package com.example.lupusincampus;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String PREFS_NAME = "FilePrefs";
    private static final String KEY_LINE_INDEX = "line_index";
    private List<String> lines = new ArrayList<>();
    private SharedPreferences prefs;
    private int currentIndex;

    public FileUtils(Context context, String fileName) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentIndex = prefs.getInt(KEY_LINE_INDEX, 0); // Recupera l'ultima riga letta
        loadFile(context, fileName);
    }

    private void loadFile(Context context, String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNextLine() {
        if (lines.isEmpty()) return "File vuoto";

        if (currentIndex >= lines.size()) {
            return "Fine del file";
        }

        String nextLine = lines.get(currentIndex);
        currentIndex++;

        // Salva la posizione per la prossima volta
        prefs.edit().putInt(KEY_LINE_INDEX, currentIndex).apply();

        return nextLine;
    }
}

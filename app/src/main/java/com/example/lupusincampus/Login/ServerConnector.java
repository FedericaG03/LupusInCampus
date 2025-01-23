package com.example.lupusincampus.Login;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnector {

    private static final String TAG = "ServerConnector";

    public void testServerConn(String serverUrl) {
        // 1. Creare un Executor per il thread in background
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // 2. Handler per comunicare con il thread principale
        Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        // 3. Eseguire il codice sul thread in background
        executor.execute(() -> {
            Log.d(TAG, "testServerConn: entrato nell'execute");
            String serverResponse;
            try {
                URL url = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // 4. Leggere la risposta dal server
                try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {

                    Scanner scanner = new Scanner(inputStream);
                    String a = scanner.nextLine();
                    serverResponse = "OK ho letto: " + a;
                    Log.d(TAG, serverResponse);
                }
            } catch (IOException e) {
                Log.e(TAG, "Errore durante la connessione: " + e.getMessage(), e);
                serverResponse = "Errore di connessione";
            }

            // 5. Passare il risultato al thread principale
            String finalResponse = serverResponse;
            mainThreadHandler.post(() -> {
                // Codice che aggiorna la UI sul thread principale
                Log.d(TAG, "Risultato: " + finalResponse);
                // Ad esempio, puoi mostrare un Toast o aggiornare una TextView
            });
        });

        executor.shutdown();
    }


    public void loginRequest(String emailOrNickname, String password, boolean isEmail, String serverUrl, LoginCallback callback) {
        // Thread separato per evitare di bloccare il UI thread
        new Thread(() -> {
            try {
                // Crea la connessione HTTP
                URL url = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Crea il corpo della richiesta in formato JSON
                JSONObject jsonBody = new JSONObject();
                if (isEmail) {
                    jsonBody.put("email", emailOrNickname);
                } else {
                    jsonBody.put("nickname", emailOrNickname);
                }
                jsonBody.put("password", password);

                // Invia la richiesta
                OutputStream os = connection.getOutputStream();
                os.write(jsonBody.toString().getBytes());
                os.flush();

                // Leggi la risposta dal server
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                // Passa la risposta al callback
                String responseString = response.toString();
                callback.onResponse(responseString);
            } catch (Exception e) {
                Log.e(TAG, "Errore durante la connessione: " + e.getMessage(), e);
                callback.onError(e);
            }
        }).start();
    }

    // Callback per la gestione della risposta dal server
    public interface LoginCallback {
        void onResponse(String jsonResponse);
        void onError(Exception e);
    }
    public void registerRequest(String nickname, String email, String password, String serverUrl, RegisterCallback callback) {
        // Thread separato per evitare di bloccare il UI thread
        new Thread(() -> {
            try {
                // Crea la connessione HTTP
                URL url = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Crea il corpo della richiesta in formato JSON
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("nickname", nickname);
                jsonBody.put("email", email);
                jsonBody.put("password", password);

                // Invia la richiesta
                OutputStream os = connection.getOutputStream();
                os.write(jsonBody.toString().getBytes());
                os.flush();

                // Leggi la risposta dal server
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                // Chiudi lo stream
                br.close();
                os.close();
                connection.disconnect();

                // Passa la risposta al callback
                String responseString = response.toString();
                callback.onResponse(responseString);

            } catch (Exception e) {
                Log.e(TAG, "Errore durante la connessione: " + e.getMessage(), e);
                callback.onError(e);
            }
        }).start();
    }

    // Callback per la registrazione
    public interface RegisterCallback {
        void onResponse(String jsonResponse);
        void onError(Exception e);
    }

    public void recoverPasswordRequest(String email, String serverUrl, PasswordRecoverCallback callback) {
        // Eseguiamo la richiesta in un thread separato per non bloccare il main thread
        new Thread(() -> {
            try {
                // Crea l'URL per la richiesta al server
                URL url = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Crea il corpo della richiesta in formato JSON (email dell'utente)
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", email); // Aggiunge l'email all'interno del JSON

                // Invia la richiesta
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(jsonBody.toString().getBytes());
                    os.flush();
                }

                // Leggi la risposta dal server
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    // Passa la risposta alla callback
                    callback.onResponse(response.toString());
                }

            } catch (IOException e) {
                Log.e(TAG, "Errore durante la connessione: " + e.getMessage(), e);
                // In caso di errore chiamano onError
                callback.onError(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // Callback per gestire la risposta
    public interface PasswordRecoverCallback {
        void onResponse(String jsonResponse);
        void onError(Exception e);
    }
}



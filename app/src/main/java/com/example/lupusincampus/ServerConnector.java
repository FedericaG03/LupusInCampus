package com.example.lupusincampus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnector {

    private static final String TAG = "ServerConnector";
    private static final String SERVER_URL = "";

    // Esegue operazioni di rete su un thread in background
    private final ExecutorService executor = Executors.newFixedThreadPool(4); //(4 thread per richieste contemporanee)
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    /**
     * Metodo generico per effettuare richieste HTTP GET
     */
    private void makeGetRequest(String endpoint,FetchDataCallback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(SERVER_URL + endpoint); // Usa l'URL base del server
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String response = readStream(connection);
                    mainThreadHandler.post(() -> callback.onSuccess(response));
                } else {
                    mainThreadHandler.post(() -> callback.onError(new IOException("Errore HTTP: " + responseCode)));
                }

            } catch (IOException e) {
                Log.e(TAG, "Errore nella richiesta GET: " + e.getMessage(), e);
                mainThreadHandler.post(() -> callback.onError(e));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    /**
     * Metodo generico per effettuare richieste HTTP POST
     */
    private void makePostRequest(String endpoint, JSONObject jsonBody, FetchDataCallback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(SERVER_URL + endpoint); // Usa l'URL base del server
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(jsonBody.toString().getBytes());
                    os.flush();
                    Log.d(TAG, "Sto a fa la richiesta POST: " );

                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String response = readStream(connection);
                    mainThreadHandler.post(() -> callback.onSuccess(response));
                } else {
                    mainThreadHandler.post(() -> callback.onError(new IOException("Errore HTTP: " + responseCode)));
                }

            } catch (IOException e) {
                Log.e(TAG, "Errore nella richiesta POST: " + e.getMessage(), e);
                mainThreadHandler.post(() -> callback.onError(e));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    /**
     * Legge i dati dalla risposta HTTP
     */
    private String readStream(HttpURLConnection connection) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    /**
     * Richiesta di login
     */
    public void loginRequest(String emailOrNickname, String password, boolean isEmail,  FetchDataCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            if (isEmail) {
                jsonBody.put("email", emailOrNickname);
            } else {
                jsonBody.put("nickname", emailOrNickname);
            }
            jsonBody.put("password", password);

            makePostRequest("/controller/player/login",jsonBody, callback);

        } catch (JSONException e) {
            callback.onError(e);
        }
    }

    /**
     * Richiesta di registrazione
     */
   public void registerRequest(String nickname, String email, String password, FetchDataCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nickname", nickname);
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            makePostRequest("/controller/player/registration", jsonBody, callback);
        } catch (JSONException e) {
            callback.onError(e);
        }
    }
/*
    public void registerRequest(String nickname, String email, String password, FetchDataCallback callback) {
        try {
            // Creazione della query string con i parametri
            String params = "nickname=" + URLEncoder.encode(nickname, "UTF-8") +
                    "&email=" + URLEncoder.encode(email, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");

            // Invio della richiesta con i parametri nell'URL (se GET) o nel body (se POST)
            makePostRequest("/controller/player/registration?" + params, null, callback);
        } catch (UnsupportedEncodingException e) {
            callback.onError(e);
        }
    }*/

    /**
     * Recupero password, (invio mail al server)
     */
    public void recoverPasswordRequest(String email, FetchDataCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);

            makePostRequest("",jsonBody, callback);
        } catch (JSONException e) {
            callback.onError(e);
        }
    }


    /**
     * Recupera la lista di inviti
     */
    public void fetchInvites(FetchDataCallback callback) {
        makeGetRequest("", callback);
    }

    /**
     * Recupera la lista di lobby disponibili
     */
    public void fetchDataForListView(FetchDataCallback callback) {
        makeGetRequest("", callback);
    }

    /**
     * Reimposta Password
     */
    public void updatePasswordRequest(JSONObject jsonBody, FetchDataCallback callback) {
        makePostRequest("", jsonBody, callback);
    }

    /**
     *Funzione per ottenere il ruolo dal server (GET)
     */
    public void fetchRole(FetchDataCallback callback) {
        makeGetRequest("/controller/game/role", callback);
    }

    /**
     * Callback generico per gestire i dati del server
     */
    public interface FetchDataCallback {
        void onSuccess(String jsonResponse);
        void onError(Exception e);
    }
}

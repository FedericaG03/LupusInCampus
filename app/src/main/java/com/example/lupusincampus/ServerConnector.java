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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnector {

    private static final String TAG = "ServerConnector";
    private static final String SERVER_URL = "http://172.19.170.102:8080";

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
     * Metodo generico per effettuare richieste HTTP PUT
     */
     private  void makePutRequest(String endpoint, JSONObject jsonBody, FetchDataCallback callback){
         executor.execute(() -> {
             HttpURLConnection connection = null;
             try {
                 URL url = new URL(SERVER_URL + endpoint); // Usa l'URL base del server
                 connection = (HttpURLConnection) url.openConnection();
                 connection.setRequestMethod("PUT");
                 connection.setRequestProperty("Content-Type", "application/json");
                 connection.setDoOutput(true);

                 try (OutputStream os = connection.getOutputStream()) {
                     os.write(jsonBody.toString().getBytes());
                     os.flush();
                     Log.d(TAG, "Sto a fa la richiesta PUT: " );
                 }

                 int responseCode = connection.getResponseCode();
                 if (responseCode == HttpURLConnection.HTTP_OK) {
                     String response = readStream(connection);
                     mainThreadHandler.post(() -> callback.onSuccess(response));
                 } else {
                     mainThreadHandler.post(() -> callback.onError(new IOException("Errore HTTP: " + responseCode)));
                 }

             } catch (IOException e) {
                 Log.e(TAG, "Errore nella richiesta PUT: " + e.getMessage(), e);
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
    //TODO: LEVARE VERIFICA MAIL O PASSWORD, LASCIARE MAIL
    public void loginRequest(String email, String password,  FetchDataCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
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

            makePutRequest("/controller/player/registration", jsonBody, callback);
        } catch (JSONException e) {
            callback.onError(e);
        }
    }
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
     * Recupera la lista di inviti lobby
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

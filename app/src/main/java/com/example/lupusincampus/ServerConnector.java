package com.example.lupusincampus;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnector {

    private static final String TAG = "ServerConnector";
    private static final String SERVER_URL = "http://193.205.162.73:8080";
    private static String sessionId = null;

    // Esegue operazioni di rete su un thread in background
    private final ExecutorService executor = Executors.newFixedThreadPool(4); //(4 thread per richieste contemporanee)
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());


    private class ResponseContent {
        protected int code;
        protected Object responseObject;

        public ResponseContent(int code, Object responseObject) {
            this.code = code;
            this.responseObject = responseObject;
        }

    }

    /**
     * Metodo generico per effettuare richieste HTTP GET
     */
    private void makeGetRequest(Context ctx, String endpoint, FetchDataCallback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(SERVER_URL + endpoint); // Usa l'URL base del server
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                sessionId = SharedActivity.getInstance(ctx).getSessionId();
                if (sessionId != null) {
                    connection.setRequestProperty("Cookie", sessionId);
                }

                int responseCode = connection.getResponseCode();
                String cookie = connection.getHeaderField("Set-Cookie");
                if (cookie != null) {
                    sessionId = cookie.split(";")[0];
                    SharedActivity.getInstance(ctx).setSessionId(sessionId);
                    Log.d(TAG, "makePostRequest: " + sessionId);
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String response = readStream(connection);
                    mainThreadHandler.post(() -> callback.onSuccess(response));
                } else {
                    mainThreadHandler.post(() -> callback.onError((new IOException("Errore HTTP: " + responseCode))));
                }

            } catch (IOException e) {
                Log.e(TAG, "Errore nella richiesta GET: " + e.getMessage(), e);
                mainThreadHandler.post(() -> callback.onServerOffline(e));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }


    /**
     * Questo metodo effettua delle richieste POST al server
     * @param ctx Contesto dell'activity chiamante
     * @param endpoint Controller del server da contattare
     * @param jsonBody Body della richiesta
     * @param callback Callback da chiamare quando arriva la risposta
     * @return Il json che il server restituisce
     */
     public void makePostRequest(Context ctx, String endpoint, JSONObject jsonBody, CallbackInterface callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(SERVER_URL + endpoint); // Usa l'URL base del server
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                sessionId = SharedActivity.getInstance(ctx).getSessionId();
                if (sessionId != null) {
                    connection.setRequestProperty("Cookie", sessionId);
                }

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(jsonBody.toString().getBytes());
                    os.flush();
                    Log.d(TAG, "makePostRequest: Costruita la richiesta: " + jsonBody.toString());
                }

                Log.d(TAG, "makePostRequest: Tento di leggere il codice di risposta");
                int responseCode = connection.getResponseCode();

                Log.d(TAG, "makePostRequest: Provo a leggere l'id sessione");
                String cookie = connection.getHeaderField("Set-Cookie");
                if (cookie != null) {
                    sessionId = cookie.split(";")[0];
                    SharedActivity.getInstance(ctx).setSessionId(sessionId);
                    Log.d(TAG, "makePostRequest, sessionId: " + sessionId);
                }

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String response = readStream(connection);
                    ResponseContent content = parseResponse(response);

                    if (content.code > 0) {
                        mainThreadHandler.post(() -> callback.onSuccess((JSONObject) content.responseObject));
                        Log.d(TAG, "onSuccess: stampo response del server " + content.responseObject.toString());
                    } else {
                        mainThreadHandler.post(() -> callback.onError((String) content.responseObject));
                    }
                } else {
                    mainThreadHandler.post(() -> callback.onServerError(new IOException("Errore: " + responseCode)));
                }
            } catch (Exception ex ) {
                Log.e(TAG, "Errore nella richiesta POST: ", ex);
                mainThreadHandler.post(() -> callback.onServerError(ex));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private ResponseContent parseResponse(String response) {
         int code;
         Object responseObject = null;
         try {
             JSONObject messagesresponse = new JSONObject(response);
             messagesresponse = messagesresponse.getJSONObject("MessagesResponse");
             Log.d(TAG, "onSuccess: stampo response server prova " + messagesresponse);

             JSONArray message = messagesresponse.getJSONArray("messages");// arriva un jsonArray
             // Primo oggetto JSON con informazioni sul successo del login
             JSONObject succesmessage = (JSONObject) message.get(0);
             succesmessage = succesmessage.getJSONObject("Enum");
             String messageInfo = succesmessage.getString("message");
             code = succesmessage.getInt("code");

             if (code > 0) {
                 responseObject = (JSONObject) message.get(1);
             } else if (code < 0) {
                 responseObject = messageInfo;
             }
         }catch (Exception ex) {
             Log.e(TAG, "parseResponse: Impossibile eseguire l'unmarshal della risposta: ", ex);
             code = 0;
             responseObject = null;
         }
         return new ResponseContent(code, responseObject);
    }

    /**
     * Metodo generico per effettuare richieste HTTP PUT
     */
    private void makePutRequest(Context ctx, String endpoint, JSONObject jsonBody, FetchDataCallback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(SERVER_URL + endpoint); // Usa l'URL base del server
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                sessionId = SharedActivity.getInstance(ctx).getSessionId();
                if (sessionId != null) {
                    connection.setRequestProperty("Cookie", sessionId);
                }

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(jsonBody.toString().getBytes());
                    os.flush();
                    Log.d(TAG, "Sto a fa la richiesta PUT: ");
                }

                int responseCode = connection.getResponseCode();
                String cookie = connection.getHeaderField("Set-Cookie");
                if (cookie != null) {
                    sessionId = cookie.split(";")[0];
                    SharedActivity.getInstance(ctx).setSessionId(sessionId);
                    Log.d(TAG, "makePostRequest: " + sessionId);
                }
                String response = readStream(connection);
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    mainThreadHandler.post(() -> callback.onSuccess(response));
                    Log.d(TAG, "onSuccess:stampo response server " + response);
                } else {
                    mainThreadHandler.post(() -> callback.onError(new IOException("Errore" +responseCode)));
                }
            } catch (IOException e) {
                Log.e(TAG, "Errore nella richiesta PUT: " + e.getMessage(), e);
                mainThreadHandler.post(() -> callback.onServerOffline(e));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }


    /**
     * Metodo che si occupa di leggere il body della risposta
     * @param connection
     * @return Restituisce il json contenente la risposta, vuoto se non riesce a leggere
     */
    private String readStream(HttpURLConnection connection){
        try (InputStream in = connection.getInputStream();
             BufferedInputStream br = new BufferedInputStream(in)){

            StringBuilder sb = new StringBuilder();
            String line;

            Scanner scanner = new Scanner(br);

            while ((scanner.hasNext())){
                line = scanner.nextLine();
                sb.append(line);
            }

            Log.d(TAG, "readStream: Letto da richiesta: " + sb.toString());
            return sb.toString();
        } catch (IOException ex) {

            Log.e(TAG, "readStream: Impossibile leggere la risposta del server: ",ex);
            return "{}";
        }
    }


    /**
     * Richiesta di registrazione
     */
    public void registerRequest(Context ctx, String nickname, String email, String password, FetchDataCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nickname", nickname);
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            makePutRequest(ctx, "/controller/player/registration", jsonBody, callback);
        } catch (JSONException e) {
            callback.onServerOffline(e);
        }
    }

    /**
     * Recupero password, (invio mail al server)
     */
    public void recoverPasswordRequest(Context ctx, String email, CallbackInterface callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);

            makePostRequest(ctx, "", jsonBody, callback);
        } catch (JSONException e) {
            callback.onServerError(e);
        }
    }


    /**
     * Recupera la lista di inviti lobby
     */
    public void fetchInvites(Context ctx, FetchDataCallback callback) {
        makeGetRequest(ctx, "", callback);
    }

    /**
     * Recupera la lista di lobby disponibili
     */
    public void fetchDataForListView(Context ctx, FetchDataCallback callback) {
        makeGetRequest(ctx, "", callback);
    }

    /**
     * Reimposta Password
     */
    public void updatePasswordRequest(Context ctx, JSONObject jsonBody, CallbackInterface callback) {
        makePostRequest(ctx, "", jsonBody, callback);
    }

    /**
     * Funzione per ottenere il ruolo dal server (GET)
     */
    public void fetchRole(Context ctx, FetchDataCallback callback) {
        makeGetRequest(ctx, "/controller/game/role", callback);
    }

    /**
     * Funzione per ottenere lista amici sal server
     */
    public void fetchDataForFriendList(Context ctx, String nickname, CallbackInterface callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nickname", nickname);
            makePostRequest(ctx, "", jsonBody, callback);
        } catch (JSONException e) {
            callback.onServerError(e);
        }
    }

    /**
     * Funzione per il logout
     */
    public void logoutReqeust(Context ctx, FetchDataCallback callback) {
        makeGetRequest(ctx, "/controller/player/logout", callback);
    }

    /**
     * Funzione per vedere il numero di amici che entrano
     */

    /**
     * Callback generico per gestire i dati del server
     */
    public interface FetchDataCallback {
        void onSuccess(String jsonResponse);

        void onError(Exception e);

        void onServerOffline(Exception e);
    }


    public interface CallbackInterface {
        void onSuccess(JSONObject jsonResponse);
        void onError(String jsonResponse);
        void onServerError(Exception e);
    }
}

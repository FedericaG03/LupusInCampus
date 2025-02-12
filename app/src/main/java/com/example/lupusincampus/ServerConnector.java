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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnector {

    private static final String TAG = "ServerConnector";
    private static final String SERVER_URL = "http://172.19.152.74:8080";
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
    public void makeGetRequest(Context ctx, String endpoint, CallbackInterface callback) {
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

                Log.d(TAG, "makeGetRequest: Provo a leggere l'id sessione");
                int responseCode = connection.getResponseCode();
                String cookie = connection.getHeaderField("Set-Cookie");
                if (cookie != null) {
                    sessionId = cookie.split(";")[0];
                    SharedActivity.getInstance(ctx).setSessionId(sessionId);
                    Log.d(TAG, "makeGetRequest: " + sessionId);
                }

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String response = readStream(connection);
                    ResponseContent content = parseResponse(response);

                    if (content.code > 0) {
                        mainThreadHandler.post(() -> callback.onSuccess(content.responseObject));
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
         List<Object> responses = new ArrayList<>();
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
                 responseObject = message.get(1);
             } else {
                 responseObject = messageInfo;
             }
         }catch (Exception ex) {
             Log.e(TAG, "parseResponse: Impossibile eseguire l'unmarshal della risposta: ", ex);
             code = 0;
             responseObject = null;
         }

         Log.d(TAG, "parseResponse: Prima di restituire l'oggetto: " + responseObject.toString());
         return new ResponseContent(code, responseObject);
    }

    /**
     * Metodo generico per effettuare richieste HTTP PUT
     */
    public void makePutRequest(Context ctx, String endpoint, JSONObject jsonBody, CallbackInterface callback) {
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
                        mainThreadHandler.post(() -> callback.onSuccess(content.responseObject));
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

    /**
     * Metodo generico per richieste DELETE
     * @param ctx
     * @param endpoint
     * @param callback
     */
    public void makeDeleteRequest(Context ctx, String endpoint,JSONObject jsonBody, CallbackInterface callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(SERVER_URL + endpoint); // Usa l'URL base del server
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
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

                Log.d(TAG, "makeDeleteRequest: Tento di leggere il codice di risposta");
                int responseCode = connection.getResponseCode();

                Log.d(TAG, "makeDeleteRequest: Provo a leggere l'id sessione");
                String cookie = connection.getHeaderField("Set-Cookie");
                if (cookie != null) {
                    sessionId = cookie.split(";")[0];
                    SharedActivity.getInstance(ctx).setSessionId(sessionId);
                    Log.d(TAG, "makeDeleteRequest, sessionId: " + sessionId);
                }

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String response = readStream(connection);
                    ResponseContent content = parseResponse(response);

                    if (content.code > 0) {
                        mainThreadHandler.post(() -> callback.onSuccess(content.responseObject));
                        Log.d(TAG, "onSuccess: stampo response del server " + content.responseObject.toString());
                    } else {
                        mainThreadHandler.post(() -> callback.onError((String) content.responseObject));
                    }
                } else {
                    mainThreadHandler.post(() -> callback.onServerError(new IOException("Errore: " + responseCode)));
                }
            } catch (Exception ex ) {
                Log.e(TAG, "Errore nella richiesta DELETE: ", ex);
                mainThreadHandler.post(() -> callback.onServerError(ex));
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
     * Recupera la lista di inviti lobby
     */
    public void fetchInvites(Context ctx, CallbackInterface callback) {
        makeGetRequest(ctx, "", callback);
    }

    /**
     * Recupera la lista di lobby disponibili
     */
    public void fetchDataForListView(Context ctx, CallbackInterface callback) {
        makeGetRequest(ctx, "", callback);
    }


    /**
     * Funzione per ottenere il ruolo dal server (GET)
     */
    public void fetchRole(Context ctx, CallbackInterface callback) {
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

    /**Callback generico per gestire i dati del server*/
    public interface CallbackInterface {
        void onSuccess(Object jsonResponse);
        void onError(String jsonResponse);
        void onServerError(Exception e);
    }
}

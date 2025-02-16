package com.example.lupusincampus.API.websocket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.lupusincampus.ServerConnector;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import java.util.ArrayList;
import java.util.List;

public class StompClientManager {
    private static final String TAG = "StompClientManager";

    private StompClient stompClient;
    private Disposable connectionDisposable;
    private List<Disposable> topicSubscriptions = new ArrayList<>();
    private WebSocketObserver webSocketObserver = WebSocketObserver.getInstance();

    private String lobbyCode;
    private static StompClientManager instance;
    public static StompClientManager getInstance(Context ctx){
        if (instance == null)
            instance = new StompClientManager(ctx);
        return instance;
    }


    public StompClientManager(Context ctx) {
        String SERVER_URL = "ws://" + new ServerConnector().getBaseIp() + ":8080/ws";
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SERVER_URL);
    }

    public void connect(String lobbyCode, String nickname) {
        List<StompHeader> headers = new ArrayList<>();
        this.lobbyCode = lobbyCode;

        connectionDisposable = stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(TAG, "WebSocket connected!");
                            subscribeToLobbyAndChat(lobbyCode, nickname);
                            break;
                        case ERROR:
                            Log.e(TAG, "WebSocket Error: ", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.d(TAG, "WebSocket Disconnected.");
                            break;
                    }
                });
        stompClient.connect(headers);
    }

    @SuppressLint("CheckResult")
    private void subscribeToLobbyAndChat(String lobbyCode, String nickname) {
        String chatTopic = "/topic/chat/" + lobbyCode;
        String lobbyTopic = "/topic/lobby/" + lobbyCode;
        String userTopic = "/topic/lobby/" + lobbyCode + "/" + nickname;

        Log.d(TAG, "Subscribing to topics: " + chatTopic + " & " + lobbyTopic);

        // Listen for chat messages
        topicSubscriptions.add(stompClient.topic(chatTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    handleTopicMessage(topicMessage);
                }, throwable -> Log.e(TAG, "Error in Chat Subscription", throwable)));

        // Listen for new players joining
        topicSubscriptions.add(stompClient.topic(lobbyTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    handleTopicLobby(topicMessage);
                    // Here you can update the UI or show a toast message
                }, throwable -> Log.e(TAG, "Error in Lobby Subscription", throwable)));

        topicSubscriptions.add(stompClient.topic(userTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage ->{
                    handleUserMessage(topicMessage);
                }, throwable -> Log.e(TAG, "subscribeToLobbyAndChat: ", throwable)));
    }

    private void handleUserMessage(StompMessage topicMessage) throws JSONException {

        JSONObject jsonObject = new JSONObject(topicMessage.getPayload());

        switch (jsonObject.getString("type")){
            case "ROLE": {

                WebSocketObserver.getInstance().notify(WebSocketObserver.EventType.ROLE, jsonObject);

                break;
            }
        }

    }

    /**
     * Method capable of handling server login messages
     * @param topicMessage
     */
    private void handleTopicLobby(StompMessage topicMessage) throws JSONException {

        JSONObject jsonObject = new JSONObject(topicMessage.getPayload());
        String player = jsonObject.getString("player");

        switch (jsonObject.getString("type")){
            case "JOIN": {

                jsonObject.put("lobbyCode", lobbyCode);
                WebSocketObserver.getInstance().notify(WebSocketObserver.EventType.PLAYER_JOINED, jsonObject);

                Log.d(TAG, "handleTopicLobby: player joined");
                break;
            }
            case "LEFT": {
                jsonObject.put("lobbyCode", lobbyCode);
                WebSocketObserver.getInstance().notify(WebSocketObserver.EventType.PLAYER_LEFT, jsonObject);
                Log.d(TAG, "handleTopicLobby: player left");
                break;
            }
        }
    }

    /**
     * Method capable of handling Chat messages between players
     * @param topicMessage
     */
    private void handleTopicMessage(StompMessage topicMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject(topicMessage.getPayload());

        if (webSocketObserver != null) {
            webSocketObserver.notify(WebSocketObserver.EventType.CHAT_MESSAGE, jsonObject);
        }
        Log.d(TAG, "handleTopicMessage: " + jsonObject.getString("sender") + " ha detto: " + jsonObject.getString("message"));
    }

    @SuppressLint("CheckResult")
    public void sendChatMessage(String sender, String message) {
        String destination = "/app/chat/" + lobbyCode;
        String payload = "{ \"sender\": \"" + sender + "\", \"message\": \"" + message + "\" }";

        Log.d(TAG, "Sending Message to " + destination + ": " + payload);

        stompClient.send(destination, payload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "Message sent successfully!"),
                        throwable -> Log.e(TAG, "Error sending message", throwable));
    }

    @SuppressLint("CheckResult")
    public void sendAck(String nickname) {
        String destination = "/app/ack";
        String payload = "{ \"nickname\": \"" + nickname + "\"}";

        Log.d(TAG, "Sending Message to " + destination + ": " + payload);

        stompClient.send(destination, payload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "Message sent successfully!"),
                        throwable -> Log.e(TAG, "Error sending message", throwable));
    }

    @SuppressLint("CheckResult")
    public void notifyLobbyJoin(String playerName) {
        String destination = "/app/lobby/" + lobbyCode;
        String payload = "{ \"player\": \"" + playerName + "\", \"type\": \"JOIN\" }";

        Log.d(TAG, "Notifying lobby " + lobbyCode + " about new player: " + playerName);

        stompClient.send(destination, payload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "Join notification sent!"),
                        throwable -> Log.e(TAG, "Error sending join notification", throwable));
    }

    @SuppressLint("CheckResult")
    public void notifyLobbyLeft(String playerName) {
        String destination = "/app/lobby/" + lobbyCode;
        String payload = "{ \"player\": \"" + playerName + "\", \"type\": \"LEFT\" }";

        Log.d(TAG, "Notifying lobby " + lobbyCode + " about new player: " + playerName);

        stompClient.send(destination, payload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "Join notification sent!"),
                        throwable -> Log.e(TAG, "Error sending join notification", throwable));
    }

    public void disconnect() {
        if (connectionDisposable != null) {
            connectionDisposable.dispose();
        }
        if (!topicSubscriptions.isEmpty()) {
            topicSubscriptions.stream().forEach(v -> v.dispose());
        }
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }
}

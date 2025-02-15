package com.example.lupusincampus.API.websocket;

public interface MessageObserver {
    void onNewMessage(String sender, String message);

}

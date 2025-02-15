package com.example.lupusincampus.API.websocket;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketObserver {

    public enum EventType {
        CHAT_MESSAGE,
        LOBBY_UPDATE
    }


    Map<EventType, List<Subscriber>> subscribers = new HashMap<>();

    private static WebSocketObserver instance;
    public static WebSocketObserver getInstance(){
        if (instance == null)
            instance = new WebSocketObserver();
        return instance;
    }


    public void subscribe(EventType event, Subscriber s){
        if (subscribers.containsKey(event)){
            subscribers.get(event).add(s);
        }
        List<Subscriber> tmp = new ArrayList<>();
        tmp.add(s);
        subscribers.put(event, tmp);
    }

    public void unsubscribe(EventType event, Subscriber s){
        List<Subscriber> subscribersList = subscribers.get(event);
        if (subscribersList != null) subscribersList.remove(s);
    }

    public void notify(EventType event, JSONObject data){
       List<Subscriber> subscribersList = subscribers.get(event);
       if (subscribersList != null) subscribersList.forEach(v -> v.update(data));
    }
}

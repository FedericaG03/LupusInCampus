package com.example.lupusincampus.API;

import com.example.lupusincampus.ServerConnector;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class LobbyAPI {
    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "LobbyAPI";



    public class LobbyCallback implements ServerConnector.CallbackInterface{

        @Override
        public void onSuccess(Object jsonResponse) {
            ;
        }

        public List<Objects> onSuccess(JSONObject response, int a){
            return null;
        }

        @Override
        public void onError(String jsonResponse) {

        }

        @Override
        public void onServerError(Exception e) {

        }
    }
}


package com.example.lupusincampus.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lupusincampus.ServerConnector;

import org.json.JSONArray;
import org.json.JSONObject;

public class FriendAPI {

    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "FriendAPI";


    public void doGetFriendsList(Context ctx) {

        requestGetFriendsList(ctx, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try {
                    JSONArray jsonResponse = (JSONArray) response;
                    Log.d(TAG, "onSuccess: ricevuto dal server: " + jsonResponse.toString(4));
                }catch (Exception e){
                    Log.e(TAG, "onSuccess: ", e);
                }
            }

            @Override
            public void onError(String jsonResponse) {
                Log.e(TAG, "onError: Errore doGetFriendsList: " + jsonResponse);
                Toast.makeText(ctx,jsonResponse,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Log.e(TAG, "onServerError: ", e);
                Toast.makeText(ctx,"Errore nel server", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void requestGetFriendsList(Context ctx, ServerConnector.CallbackInterface callback){
        serverConnector.makeGetRequest(ctx, "/controller/friend/get-friends-list", callback);
    }

}

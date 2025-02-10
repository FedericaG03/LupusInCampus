package com.example.lupusincampus.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lupusincampus.ServerConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendAPI {

    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "FriendAPI";

    //TODO: TESTARE QUESTA CLASSE
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


    public void doSendFriendRequest(Context ctx, int friendId) {
        requestSendFriendRequest(ctx, friendId, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try {
                    JSONObject jsonResponse = (JSONObject) response;
                    Log.d(TAG, "onSuccess: risposta dal server: " + jsonResponse.toString(4));
                    Toast.makeText(ctx, "Richiesta di amicizia inviata!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: errore nel parsing della risposta", e);
                }
            }

            @Override
            public void onError(String jsonResponse) {
                Log.e(TAG, "onError: Errore doSendFriendRequest: " + jsonResponse);
                Toast.makeText(ctx, jsonResponse, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Log.e(TAG, "onServerError: ", e);
                Toast.makeText(ctx, "Errore nel server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestSendFriendRequest(Context ctx, int id, ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makePostRequest(ctx, "/controller/friend/send-friend-request", jsonObject, callback);
    }

    public void doAcceptFriendRequest(Context ctx, int id) {
        requestAcceptFriendRequest(ctx, id, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try {
                    JSONObject jsonResponse = (JSONObject) response;
                    Log.d(TAG, "onSuccess: risposta dal server: " + jsonResponse.toString(4));
                    Toast.makeText(ctx, "Richiesta di amicizia accettata!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: errore nel parsing della risposta", e);
                }
            }

            @Override
            public void onError(String jsonResponse) {
                Log.e(TAG, "onError: Errore doAcceptFriendRequest: " + jsonResponse);
                Toast.makeText(ctx, jsonResponse, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Log.e(TAG, "onServerError: ", e);
                Toast.makeText(ctx, "Errore nel server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestAcceptFriendRequest(Context ctx, int id, ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makePostRequest(ctx, "/controller/friend/accept-friend-request", jsonObject, callback);
    }
}


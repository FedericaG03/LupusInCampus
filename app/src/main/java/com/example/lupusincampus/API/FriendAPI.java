package com.example.lupusincampus.API;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

                    List<Player> friendList = SharedActivity.getInstance(ctx).getFriendList();
                    friendList.clear();

                    for (int i = 0; i < jsonResponse.length(); i++){
                        JSONObject playerJson = ((JSONObject) jsonResponse.get(i)).getJSONObject("player");

                        Player p = new Player();
                        p.setId(playerJson.getInt("id"));
                        p.setNickname(playerJson.getString("nickname"));
                        p.setEmail(playerJson.getString("email"));

                        friendList.add(p);
                    }

                    Log.d(TAG, "onSuccess: Post friend parse: " + friendList.toString());
                    SharedActivity.getInstance(ctx).setFriendList(friendList);

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
            jsonObject.put("friendId", id);
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
            //TODO aggiustare
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makePostRequest(ctx, "/controller/friend/friend-request-result", jsonObject, callback);
    }


    /***
     * Funzione che server per rimuovere un amico dalla lista
     * @param ctx
     * @param friendId
     */
    public void doRemoveFriend(Context ctx, int friendId) {
        requestRemoveFriend(ctx, friendId, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try {
                    JSONObject jsonResponse = (JSONObject) response;
                    Log.d(TAG, "onSuccess: risposta dal server: " + jsonResponse.toString(4));
                    Toast.makeText(ctx, "Amico rimosso con successo!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: errore nel parsing della risposta", e);
                }
            }

            @Override
            public void onError(String jsonResponse) {
                Log.e(TAG, "onError: Errore doRemoveFriend: " + jsonResponse);
                Toast.makeText(ctx, jsonResponse, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Log.e(TAG, "onServerError: ", e);
                Toast.makeText(ctx, "Errore nel server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestRemoveFriend(Context ctx, int id, ServerConnector.CallbackInterface callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        serverConnector.makePostRequest(ctx, "/controller/friend/remove-friend", jsonObject, callback);
    }

    public void doSearchPlayer(Context ctx, String nickname){
        requestGetSearchPlayer(ctx,nickname, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object response) {
                try {
                    JSONArray jsonResponse = (JSONArray) response;
                    Log.d(TAG, "onSuccess: ricevuto dal server: " + jsonResponse.toString(4));

                    List<Player> searchResult = SharedActivity.getInstance(ctx).getSearchResult();
                    searchResult.clear();

                    for(int i = 0; i < jsonResponse.length(); i++){
                        JSONObject playerJson = ((JSONObject) jsonResponse.get(i)).getJSONObject("player");

                        Player p = new Player();
                        p.setId(playerJson.getInt("id"));
                        p.setNickname(playerJson.getString("nickname"));
                        p.setEmail(playerJson.getString("email"));

                        searchResult.add(p);
                    }
                    Log.d(TAG, "onSuccess: Post friend parse: " + searchResult.toString());
                    SharedActivity.getInstance(ctx).setSearchResult(searchResult);


                }catch (Exception e){
                    Log.d(TAG, "onSuccess", e);
                }
            }

            @Override
            public void onError(String jsonResponse) {
                Log.e(TAG, "onError: Errore doSearchPlayer: " + jsonResponse);
                Toast.makeText(ctx,jsonResponse,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerError(Exception e) {
                Log.e(TAG, "onServerError: ", e);
                Toast.makeText(ctx,"Errore nel server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void requestGetSearchPlayer(Context ctx, String nickname, ServerConnector.CallbackInterface callback){
        serverConnector.makeGetRequest(ctx, "/controller/friend/search?query=" + Uri.encode(nickname), callback);
    }
}


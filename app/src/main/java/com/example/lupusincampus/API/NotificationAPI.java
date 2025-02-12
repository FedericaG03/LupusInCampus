package com.example.lupusincampus.API;

import android.content.Context;
import android.util.Log;

import com.example.lupusincampus.MainActivity;
import com.example.lupusincampus.ServerConnector;
import com.example.lupusincampus.SharedActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;

public class NotificationAPI {



    private static ServerConnector serverConnector = new ServerConnector();
    private static final String TAG = "NotificationAPI";
    private static boolean FLAG = false;



    public void doSave(Context ctx){

        saveRequest(ctx, new ServerConnector.CallbackInterface() {
            @Override
            public void onSuccess(Object jsonResponse) {
                Log.d(TAG, "onSuccess: jsonResponse from save token: " + jsonResponse.toString());
            }

            @Override
            public void onError(String jsonResponse) {

            }

            @Override
            public void onServerError(Exception e) {

            }
        });

    }

    public void saveRequest(Context ctx, ServerConnector.CallbackInterface callback){

        JSONObject body = new JSONObject();
        String deviceToken = null;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String pushyToken = Pushy.register(ctx);
                Log.d(TAG, "Pushy Token: " + pushyToken);
                try {
                    body.put("playerId", SharedActivity.getInstance(ctx).getId());
                    body.put("token", pushyToken);
                } catch (JSONException ex){
                    Log.e(TAG, "saveRequest: Cannot create boy for save Token Request", ex);
                }

                serverConnector.makePutRequest(ctx,"/controller/notification/save-token", body ,callback);
            } catch (Exception e) {
                Log.e(TAG, "Pushy registration failed: " + e.getMessage());
            }
        });


    }


}

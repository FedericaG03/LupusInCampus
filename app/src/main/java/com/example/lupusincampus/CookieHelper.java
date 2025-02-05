package com.example.lupusincampus;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class CookieHelper {
    public static void init() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }
}
package com.example.eskild.hc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Eskild on 26.01.2018.
 */
public class Session {
    private String email;
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
    }
    public void setToken(String Token) {
        prefs.edit().putString("Token", Token).commit();
    }

    public String getusename() {
        String usename = prefs.getString("usename", "");
        return usename;
    }
    public String getToken() {
        String usename = prefs.getString("Token", "");
        return usename;
    }
    public boolean Logout(){
        prefs.edit().putString("username", null).commit();
        prefs.edit().putString("Token", null).commit();
        if (this.getToken()== null){
            if (this.getusename() == null){
                return true;
            }
        }
        return false;
    }
}
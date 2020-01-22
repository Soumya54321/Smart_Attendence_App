package com.example.attendance;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    private final String SHARED_PREFS = "sharedPrefs";
    private final String LOGGED_IN = "loggedIn";
    private final String NAME = "name";
//    private static boolean loggedIn = false;

    public void setLoggedIn(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGGED_IN, !sharedPreferences.getBoolean(LOGGED_IN, false));
        editor.apply();

    }

    public void setEmail(Context context, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public boolean getLoggedInStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(LOGGED_IN, false);
    }

    public String getEmail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        return sharedPreferences.getString(NAME, "");
    }
}

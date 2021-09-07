/*
 * *
 *  * Created by Divya Shree R
 *  * Copyright (c) 21-07-2018 . All rights reserved.
 *
 */

package com.mazenet.mzs119.mpvmemberapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 17-Apr-17.
 */

public class GlobalValue {

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Track";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    public GlobalValue(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String email){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public static void putString(final String key, final String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(final String key) {
        return pref.getString(key, null);
    }

    public static void ClearAll(){
        editor.clear();
        editor.commit();
    }

    public static void Clear(String key){
        editor.remove(key);
        editor.apply();
    }


}

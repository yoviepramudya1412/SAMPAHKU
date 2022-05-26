package com.example.Sayang;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.w3c.dom.Text;

public class preferences {
    private static final String DATA_LOGIN = "status_login",DATA_AS = "as";

    private static SharedPreferences getSharedRererences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    static void setDataAs(Context context, String data){
        SharedPreferences.Editor editor = getSharedRererences(context).edit();
        editor.putString(DATA_AS,data);
        editor.apply();
    }

    static String getDataAs(Context context){
        return getSharedRererences(context).getString(DATA_AS,"");
    }

    public static void  setDataLogin(Context context , boolean status) {
        SharedPreferences.Editor editor = getSharedRererences(context).edit();
        editor.putBoolean(DATA_LOGIN,status);
        editor.apply();
    }
    public  static boolean getDataLogin(Context context){
        return getSharedRererences(context).getBoolean(DATA_LOGIN,false);
    }
    public static void clearData(Context context){
        SharedPreferences.Editor editor = getSharedRererences(context).edit();
        editor.remove(DATA_AS);
        editor.remove(DATA_LOGIN);
        editor.apply();

    }
}

package com.test.hfsimple;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2017/4/7.
 */
public class ParaSave {

    private Context context ;

    public ParaSave(Context context) {
        this.context = context ;
    }

    public void saveSerial(String serial){
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        SharedPreferences.Editor editor = shared.edit() ;
        editor.putString("serial", serial) ;
        editor.commit() ;

    }

    public void saveAndroidId(String symbol){
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        SharedPreferences.Editor editor = shared.edit() ;
        editor.putString("android", symbol) ;
        editor.commit() ;

    }
    public String getAndroidId() {
        String symbol = "" ;
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        symbol = shared.getString("android", "") ;
        return symbol ;
    }

    public void saveRegister(String symbol){
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        SharedPreferences.Editor editor = shared.edit() ;
        editor.putString("register", symbol) ;
        editor.commit() ;

    }

    public String getRegister() {
        String symbol = "" ;
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        symbol = shared.getString("register", "") ;
        return symbol ;
    }


    public void saveSymbol(String symbol){
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        SharedPreferences.Editor editor = shared.edit() ;
        editor.putString("symbol", symbol) ;
        editor.commit() ;

    }




    public String getSerial() {
        String serial = "" ;
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        serial = shared.getString("serial", "com14") ;
        return serial ;
    }

    public String getSymbol() {
        String symbol = "" ;
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        symbol = shared.getString("symbol", "0D0A") ;
        return symbol ;
    }


    public void savePower(String power){
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE) ;
        SharedPreferences.Editor editor = shared.edit() ;
        editor.putString("power", power) ;
        editor.commit() ;

    }

    public String getPower() {
        String power = "" ;
        SharedPreferences shared = context.getSharedPreferences("para", Context.MODE_PRIVATE);
        power = shared.getString("power", "psam power");
        return power ;
    }
}

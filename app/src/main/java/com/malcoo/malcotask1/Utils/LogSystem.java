package com.malcoo.malcotask1.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malcoo.malcotask1.Model.Log;

import java.util.ArrayList;

public class LogSystem {

     static LogSystem logSystem;
     private final Gson gson;
     private SharedPreferences sharedPreferences;
     private SharedPreferences.Editor editor;
     public static final String LOG_KEY="logs";

     @SuppressLint("CommitPrefEdits")
     private LogSystem(Context context) {
         gson=new Gson();
         sharedPreferences=context.getSharedPreferences("logs",Context.MODE_PRIVATE);
         editor=sharedPreferences.edit();
     }

     public static LogSystem getInstance(Context context){
        return logSystem==null?logSystem=new LogSystem(context):logSystem;
    }

    public void addEnteringTime(long enteringTime){
         String json=sharedPreferences.getString(LOG_KEY,"");
         ArrayList<Log> logs=getLogList(json);
         Log log=new Log();
         log.setEnteringTime(enteringTime);
         logs.add(log);
         String newJson=toJson(logs);
         editor.putString(LOG_KEY,newJson).apply();


    }
    public boolean addLeavingTime(long leavingTime){
        String json=sharedPreferences.getString(LOG_KEY,"");
        ArrayList<Log> logs=getLogList(json);
        if (logs.isEmpty()){
            return false;}
        else {
            Log log=logs.get(logs.size()-1);
            if (log.getLeavingTime()!=-1) return false;
            log.setLeavingTime(888);
            logs.set(logs.size()-1,log);
            editor.putString(LOG_KEY,toJson(logs)).apply();

            return true;
        }
    }

    private String toJson(ArrayList<Log> log){
         return gson.toJson(log);
    }

    private ArrayList<Log> getLogList(String json){
        ArrayList<Log> logs= gson.fromJson(json,new TypeToken<ArrayList<Log>>(){}.getType());
        if (logs==null)return new ArrayList<Log>();
        return logs;
    }

    public String print(){
        return sharedPreferences.getString(LOG_KEY,"");

    }

}

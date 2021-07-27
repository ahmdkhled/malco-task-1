package com.malcoo.malcotask1.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malcoo.malcotask1.Model.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LogSystem {

     static LogSystem logSystem;
     private final Gson gson;
     private SharedPreferences sharedPreferences;
     private SharedPreferences.Editor editor;
     public static final String LOG_KEY="logs";
     public static final String STATUS_KEY="status";
     public static final int IN_CIRCLE=1;
     public static final int OUT_OF_CIRCLE=0;
     public static final int UNKNOWN=-1;
    private static final String TAG = "LogSystem";

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
         if (getStatus()==IN_CIRCLE || enteredToday()) return;
         setStatus(IN_CIRCLE);
         String json=sharedPreferences.getString(LOG_KEY,"");
         ArrayList<Log> logs=getLogList(json);
         Log log=new Log();
         log.setEnteringTime(enteringTime);
         logs.add(log);
         String newJson=toJson(logs);
         editor.putString(LOG_KEY,newJson).apply();
         android.util.Log.d(TAG, "addEnteringTime: "+print());


    }
    public boolean addLeavingTime(long leavingTime){
         if (getStatus()==OUT_OF_CIRCLE)return false;
         setStatus(OUT_OF_CIRCLE);
        String json=sharedPreferences.getString(LOG_KEY,"");
        ArrayList<Log> logs=getLogList(json);
        if (logs.isEmpty()){
            return false;}
        else {
            Log log=logs.get(logs.size()-1);
            if (log.getLeavingTime()!=-1) return false;
            log.setLeavingTime(leavingTime);
            logs.set(logs.size()-1,log);
            editor.putString(LOG_KEY,toJson(logs)).apply();
            android.util.Log.d(TAG, "addLeavingTime: ");
            return true;
        }
    }

    public void setStatus(int status){
         editor.putInt(STATUS_KEY,status).apply();
    }

    public int getStatus(){
         return sharedPreferences.getInt(STATUS_KEY,UNKNOWN);
    }

    private String toJson(ArrayList<Log> log){
         return gson.toJson(log);
    }

    private ArrayList<Log> getLogList(String json){
        ArrayList<Log> logs= gson.fromJson(json,new TypeToken<ArrayList<Log>>(){}.getType());
        if (logs==null)return new ArrayList<Log>();
        return logs;
    }

    private boolean enteredToday(){
        ArrayList<Log> logs=getLogList(print());
        if (logs==null||logs.isEmpty()){
            return false;
        }
        for (Log log:logs){
            String enteringDay=toDate(log.getEnteringTime());
            String currentDay=toDate(System.currentTimeMillis());
            if (enteringDay.equals(currentDay))return true;
        }

        return false;
    }

    private String toDate(long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd-MM-yyyy", cal).toString();
    }

    public String print(){
        return sharedPreferences.getString(LOG_KEY,"");

    }

    public void clear(){

         editor.clear();
    }

}

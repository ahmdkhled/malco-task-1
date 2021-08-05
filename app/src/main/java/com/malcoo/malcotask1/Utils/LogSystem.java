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
import java.util.Date;

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

    public boolean addEnteringTime(Long enteringTime){
         if (getStatus()==IN_CIRCLE ) return false;
         setStatus(IN_CIRCLE);
         String json=sharedPreferences.getString(LOG_KEY,"");
         ArrayList<Log> logs=getLogList(json);
         Log log=new Log();
         log.setEnteringTime(enteringTime);
         logs.add(log);
         String newJson=toJson(logs);
         editor.putString(LOG_KEY,newJson).apply();
         android.util.Log.d(TAG, "addEnteringTime: "+print());
        return true;

    }
    public long addLeavingTime(Long leavingTime){
         if (getStatus()==OUT_OF_CIRCLE)return -1;
         setStatus(OUT_OF_CIRCLE);
        String json=sharedPreferences.getString(LOG_KEY,"");
        ArrayList<Log> logs=getLogList(json);
        if (logs.isEmpty()){
            return -1;}
        else {
            Log log=logs.get(logs.size()-1);
            if (log.getLeavingTime()!=null) return -1;
            log.setLeavingTime(leavingTime);
            logs.set(logs.size()-1,log);
            editor.putString(LOG_KEY,toJson(logs)).apply();
            android.util.Log.d(TAG, "addLeavingTime: ");
            return log.getEnteringTime();
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

    public static String toTime(long timeStamp){

        return new SimpleDateFormat("hh:mm:ss a").format(new Date(timeStamp));

    }

    public String log(long enteringTimeStamp, Long leavingTimeStamp){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(enteringTimeStamp);
        String enteringDay= DaysOfWeek.values()[c.get(Calendar.DAY_OF_WEEK)].name();
        String enteringTime=new SimpleDateFormat("hh:mm:ss a").format(new Date(enteringTimeStamp));
        String leavingTime="";
        if (leavingTimeStamp!=null)
         leavingTime=new SimpleDateFormat("hh:mm:ss a").format(new Date(leavingTimeStamp));

        return enteringDay +" >> "+enteringTime +" >> "+leavingTime;

    }

    public String print(){
        return sharedPreferences.getString(LOG_KEY,"");
    }

    @SafeVarargs
    public final String log(ArrayList<Log>... logsList){
         ArrayList<Log> logs=new ArrayList<>();
         if(logsList.length==0){
             logs=getLogList(print());
         }else {
             logs=logsList[0];
         }
         StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("\n-------------- printing the log ----------------- \n");
         for (Log log:logs){
            stringBuilder.append(log(log.getEnteringTime(),log.leavingTime));
            stringBuilder.append("\n");
         }
         stringBuilder.append("\n -------------------end of log -----------------------");
         return stringBuilder.toString();
    }


    public String logToday(){
        ArrayList<Log> logs=getLogList(print());
        ArrayList<Log> todayLogs=new ArrayList<>();

        for (Log log:logs){
            String enteringDay=toDate(log.getEnteringTime());
            String currentDay=toDate(System.currentTimeMillis());
            if (enteringDay.equals(currentDay))
                todayLogs.add(log);
        }
        return log(todayLogs);
    }

    public void clear(){

         editor.clear();
    }

    private enum DaysOfWeek{
        SATURDAY,
        SUNDAY,
         MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY

    }

}

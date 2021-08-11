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
    public static final int CHECK_IN=1;
    public static final int CHECK_OUT=2;
    public static final String CHECKIN_STATUS_TAG="checkin_status";



    @SuppressLint("CommitPrefEdits")
     private LogSystem(Context context) {
         gson=new Gson();
         sharedPreferences=context.getSharedPreferences("logs",Context.MODE_PRIVATE);
         editor=sharedPreferences.edit();
     }

     //singleton pattern
     public static LogSystem getInstance(Context context){
        return logSystem==null?logSystem=new LogSystem(context):logSystem;
    }

    // get log as json string from shared preferences
    public String print(){
        return sharedPreferences.getString(LOG_KEY,"");
    }

    // record entering time as timestamp
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
        return true;

    }
    // record leaving time as timestamp
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
            return log.getEnteringTime();
        }
    }

    // store status if he is inside circle or not
    public void setStatus(int status){
         editor.putInt(STATUS_KEY,status).apply();
    }

    public int getStatus(){
         return sharedPreferences.getInt(STATUS_KEY,UNKNOWN);
    }

    // convert list of log  to json string
    private String toJson(ArrayList<Log> log){
         return gson.toJson(log);
    }

    //convert json string to list of log
    private ArrayList<Log> getLogList(String json){
        ArrayList<Log> logs= gson.fromJson(json,new TypeToken<ArrayList<Log>>(){}.getType());
        if (logs==null)return new ArrayList<Log>();
        return logs;
    }

    //convert timestamp to date
    private String toDate(long timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd-MM-yyyy", cal).toString();
    }

    //convert timestamp to time
    public static String toTime(long timeStamp){
        return new SimpleDateFormat("hh:mm:ss a").format(new Date(timeStamp));
    }

    // print one log of entering and leaving time
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


    //print whole user's log (entering time => leaving time)
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

    //read the today's only user attendance log
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

    public void clear(){ editor.clear(); }

    public int getLastStatus(){
        return sharedPreferences.getInt(CHECKIN_STATUS_TAG,-1);
    }
    //save the last user status if he checked in or checked out
    public void setLastStatus(int status){
         editor.putInt(CHECKIN_STATUS_TAG,status).commit();
    }


    private enum DaysOfWeek{
        SATURDAY, SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
    }

}

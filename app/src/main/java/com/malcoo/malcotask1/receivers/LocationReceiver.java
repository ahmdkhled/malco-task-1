package com.malcoo.malcotask1.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.malcoo.malcotask1.Repo.LocationRepo;

public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationReceiver";
    static Boolean isEnabled=null;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
            boolean locationEnabled= LocationRepo.getInstance(context).isLocationEnabled();
            Log.d(TAG, "onReceive: "+locationEnabled);
            if (isEnabled==null||isEnabled!=locationEnabled)
            LocationRepo.getInstance(context).setLocationEnabled(locationEnabled);
            isEnabled=locationEnabled;
        }
    }
}

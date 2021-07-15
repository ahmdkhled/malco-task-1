package com.malcoo.malcotask1.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;
import com.malcoo.malcotask1.Repo.LocationRepo;

public class TransitionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Transition", "onReceive: ");
        if (ActivityTransitionResult.hasResult(intent)) {

            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);

            for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                String transition = LocationRepo.TransitionToString(event.getActivityType());
                String info = "Transition: " + transition +
                        " (" + (event.getTransitionType()) + ")" ;
                Log.d("Transition", ": "+info);
                Toast.makeText(context, info, Toast.LENGTH_SHORT).show();

            }
        }else {
            Log.d("Transition", "no result: ");
        }
    }
}

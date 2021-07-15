package com.malcoo.malcotask1.Repo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.malcoo.malcotask1.BuildConfig;
import com.malcoo.malcotask1.Model.Result;
import com.malcoo.malcotask1.Utils.TransitionReceiver;
import com.malcoo.malcotask1.views.MapsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static android.content.Context.LOCATION_SERVICE;


public class LocationRepo {

    Context context;
    private static LocationRepo locationRepo;
    MutableLiveData<Result<Location>> locationData;
    public static final String TRANSITIONS_RECEIVER_ACTION =
            BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION";

    private LocationRepo(Context context) {
        this.context = context;
    }

    public static LocationRepo getInstance(Context context) {
        if (locationRepo==null){
            locationRepo=new LocationRepo(context);
        }
        return locationRepo;
    }



    @SuppressLint("MissingPermission")
    public MutableLiveData<Result<Location>>  trackLocation(){
        locationData=new MutableLiveData<>();

        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(30000); // 5 seconds just for testing
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationCallback locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                locationData.setValue(Result.SUCCESS(locationResult.getLastLocation()));
                Log.d("trackLocation", "onLocationResult: "+locationResult.getLastLocation());
            }


        };
        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest,locationCallback,null);
        return locationData;

    }

    public Boolean isLocationEnabled() {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    public void registerActivityTransition(){
        ArrayList<ActivityTransition> activityTransitionList=new ArrayList<>();
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());





        ActivityTransitionRequest request = new ActivityTransitionRequest(activityTransitionList);

        Intent intent = new Intent(context, TransitionReceiver.class);
        //intent.setAction(TRANSITIONS_RECEIVER_ACTION);
        PendingIntent transitionsPendingIntent =
                PendingIntent.getBroadcast(context, 112, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Task<Void> task =
                ActivityRecognition.getClient(context)
                        .requestActivityTransitionUpdates(request, transitionsPendingIntent);

        task.addOnCompleteListener(task1 -> {
            Log.d("TAG", "registered ActivityTransition: "+task.isSuccessful());
        });
        task.addOnFailureListener(
                e -> Log.e("Transition", "Transitions Api could NOT be registered: " + e));


        ActivityRecognition.getClient(context).requestActivityTransitionUpdates(request,transitionsPendingIntent)
                .addOnCompleteListener(task12 ->

                        Log.d("Transition", "registerActivityTransition: ")
                );


    }

    static public LatLng toLatLng(Location location){
        return new LatLng(location.getLatitude(),location.getLongitude());
    }

    public static String TransitionToString(int activity) {
        switch (activity) {
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            default:
                return "UNKNOWN";
        }
    }

    public static String ActivityToString(int transitionType) {
        switch (transitionType) {
            case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                return "ENTER";
            case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "UNKNOWN";
        }
    }
}

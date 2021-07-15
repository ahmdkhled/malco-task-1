package com.malcoo.malcotask1.Repo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.BuildConfig;
import com.malcoo.malcotask1.Model.Result;


public class LocationRepo {

    Context context;
    private static LocationRepo locationRepo;
    MutableLiveData<Result<Location>> locationData=new MutableLiveData<>();

    LocationCallback locationCallback;

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

        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(5000); // 5 seconds just for testing
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                locationData.setValue(Result.SUCCESS(locationResult.getLastLocation()));
                Log.d("trackLocation", "Timer: in repo"+locationData.getValue());
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

    public void stopLocationUpdate(){
        LocationServices.getFusedLocationProviderClient(context)
                .removeLocationUpdates(locationCallback);
    }

    static public LatLng toLatLng(Location location){
        return new LatLng(location.getLatitude(),location.getLongitude());
    }


}

package com.malcoo.malcotask1.Repo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.malcoo.malcotask1.Model.Result;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static android.content.Context.LOCATION_SERVICE;


public class LocationRepo {

    Context context;
    private static LocationRepo locationRepo;
    MutableLiveData<Result<LatLng>> locationData;

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
    public MutableLiveData<Result<LatLng>> getLocation(){
        locationData=new MutableLiveData<>();


        LocationRequest mLocationRequest = LocationRequest.create();

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location =locationResult.getLastLocation();
                Log.d("TAG", "getLocation: "+location);
                if (location!=null){
                    LatLng cordinates= new LatLng(location.getLatitude(),location.getLongitude());
                    locationData.setValue(Result.SUCCESS(cordinates));
                }



            }
        };
        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
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
}

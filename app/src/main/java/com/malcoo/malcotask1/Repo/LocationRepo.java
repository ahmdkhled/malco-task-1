package com.malcoo.malcotask1.Repo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import static android.content.Context.LOCATION_SERVICE;


public class LocationRepo {

    Context context;
    private static LocationRepo locationRepo;

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
    public LatLng getLocation(){

        LocationManager locationManager= (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Location location=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        //Log.d("TAGGG", "getLocation: "+location.getLatitude()+"  --  "+location.getLongitude());
        if (location!=null){
            return new LatLng(location.getLatitude(),location.getLongitude());
        }
        return null;
    }
}

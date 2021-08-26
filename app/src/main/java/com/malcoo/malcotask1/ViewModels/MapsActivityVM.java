package com.malcoo.malcotask1.ViewModels;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.Model.DirectionResponse;
import com.malcoo.malcotask1.Model.Result;
import com.malcoo.malcotask1.Repo.CheckInRepo;
import com.malcoo.malcotask1.Repo.DirectionsRepo;
import com.malcoo.malcotask1.Repo.LocationRepo;
import com.malcoo.malcotask1.Utils.LogSystem;

public class MapsActivityVM extends AndroidViewModel {

    public boolean firstLocationReq=true;

    public MapsActivityVM(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Result<Location>> getLocation() {
        return LocationRepo.getInstance(getApplication().getApplicationContext())
                .trackLocation();

    }

    public void stopLocationUpdates(){
        LocationRepo.getInstance(getApplication().getApplicationContext())
                .stopLocationUpdate();
    }

    public boolean isLocationEnabled(){
        return LocationRepo.getInstance(getApplication().getApplicationContext())
                .isLocationEnabled();

    }

    public int getLastStatus(){
        return LogSystem.getInstance(getApplication().getApplicationContext()).getLastStatus();
    }

    public MutableLiveData<Result<DirectionResponse>> getDirections(Context context, LatLng origin, LatLng dest){
        return DirectionsRepo.getInstance().getDirections(context,origin,dest);
    }

    }

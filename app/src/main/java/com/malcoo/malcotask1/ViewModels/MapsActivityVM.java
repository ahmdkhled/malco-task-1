package com.malcoo.malcotask1.ViewModels;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.Model.Result;
import com.malcoo.malcotask1.Repo.LocationRepo;

public class MapsActivityVM extends AndroidViewModel {

    public boolean firstLocationReq=true;

    public MapsActivityVM(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Result<Location>> getLocation() {
        return LocationRepo.getInstance(getApplication().getApplicationContext())
                .trackLocation();

    }

    public boolean isLocationEnabled(){
        return LocationRepo.getInstance(getApplication().getApplicationContext())
                .isLocationEnabled();

    }


}

package com.malcoo.malcotask1.Repo;

import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.LatLng;

public class CheckInRepo {

    private static  CheckInRepo checkInRepo;
    private MutableLiveData<Integer> checkInStatus=new MutableLiveData<>();
    private MutableLiveData<LatLng> lastLocation=new MutableLiveData<>();


    private CheckInRepo(){}

    public static CheckInRepo getInstance(){
        return checkInRepo==null?checkInRepo=new CheckInRepo():checkInRepo;
    }

    public void setCheckInStatus(int status) {
        checkInStatus.setValue(status);
    }

    public MutableLiveData<Integer> getCheckInStatus() {
        return checkInStatus;
    }





}

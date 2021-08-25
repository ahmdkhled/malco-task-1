package com.malcoo.malcotask1.views;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Repo.CheckInRepo;
import com.malcoo.malcotask1.Repo.LocationRepo;
import com.malcoo.malcotask1.Utils.CameraUtil;
import com.malcoo.malcotask1.Utils.FragmentUtils;
import com.malcoo.malcotask1.Utils.LogSystem;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.databinding.FragCheckInBinding;

import static com.malcoo.malcotask1.Utils.LogSystem.CHECK_IN;
import static com.malcoo.malcotask1.Utils.LogSystem.CHECK_OUT;

public class CheckInFrag extends Fragment implements LocationBottomSheet.OnActivateLocationClickedListener{

    FragCheckInBinding binding;
    ActivityResultLauncher<Intent> launcher;
    LocationBottomSheet locationBottomSheet;
    LatLng currentLocation;
    LogSystem logSystem;
    boolean forceCheckout;
    int status;
    private static final String TAG = "CheckInFrag";


    CheckInFrag(LatLng currentLocation,int status){
        this.currentLocation=currentLocation;
        this.status=status;
    }

    public CheckInFrag(LatLng currentLocation, int status, boolean forceCheckout) {
        this.currentLocation = currentLocation;
        this.forceCheckout = forceCheckout;
        this.status = status;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.frag_check_in,container,false);
        logSystem=LogSystem.getInstance(getContext());

        locationBottomSheet=new LocationBottomSheet(this);

        checkLocation();


        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (LocationRepo.getInstance(getContext()).isLocationEnabled()){
                        observeLocation();
                        return;
                    }
                    locationBottomSheet.show(getChildFragmentManager(),"");
                });


        return binding.getRoot();
    }

    private void checkLocation(){
        boolean locationEnabled=LocationRepo.getInstance(getContext()).isLocationEnabled();
        if (locationEnabled){
            observeLocation();
        }else{
            locationBottomSheet.show(getChildFragmentManager(),"");
        }
    }

    private  void observeLocation(){
        LocationRepo.getInstance(getContext())
                .getLocationData()
                .observe(getViewLifecycleOwner(),locationRes->{
                    Location location=locationRes.getData();
                    if (locationRes.isSuccess()&&location!=null){
                        currentLocation=LocationRepo.toLatLng(location);
                        Log.d(TAG, "location in scanner: "+currentLocation);

                    }
                });
    }

    private void observeQrCode(){
        CameraUtil.getInstance().startCamera(getContext(),this,binding.previewView);
        CameraUtil.getInstance().setOnBarcodeScannedListener(barcode -> {
            Log.d(TAG, "observeQrCode: "+barcode.getRawValue());
            if (!LocationRepo.getInstance(getContext()).isLocationEnabled()){
                locationBottomSheet.show(getChildFragmentManager(),"");
                binding.barcodeValue.setText("please enable location ");
                return;
            }

            String value=barcode.getRawValue();

            LatLng coordinates= MapUtil.getCoordinates(value);
            if (coordinates==null){
                binding.barcodeValue.setText(R.string.wrong_qr);
                return;
            }
            float distance = MapUtil.getDistanceBetween(currentLocation,coordinates);
            if (distance<=200){
                CameraUtil.getInstance().stopAnalyzer();
                if (status==CHECK_IN) checkOut();
                else if (status==CHECK_OUT) checkIn();
            }else {
                binding.barcodeValue.setText(R.string.away_from_warehouse);
            }

        });
    }

    private void checkIn(){
        long currentTime=System.currentTimeMillis();
        boolean b=logSystem.addEnteringTime(currentTime);
        FragmentUtils.replaceFragment(getContext(),new CheckedInFrag(status,currentLocation,currentTime,forceCheckout));
        CheckInRepo.getInstance()
                .setCheckInStatus(CHECK_IN);
        LogSystem.getInstance(getContext()).setLastStatus(CHECK_IN);
    }

    private void checkOut() {
        long currentTime=System.currentTimeMillis();
        logSystem.addLeavingTime(currentTime);
        FragmentUtils.replaceFragment(getContext(),new CheckedInFrag(status,currentLocation,currentTime,forceCheckout));
        CheckInRepo.getInstance()
                .setCheckInStatus(CHECK_OUT);
        LogSystem.getInstance(getContext()).setLastStatus(CHECK_OUT);

    }

    public void setForceCheckout(boolean forceCheckout) {
        this.forceCheckout = forceCheckout;
    }

    @Override
    public void onActivateLocationClicked() {
        launcher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void onActivateLocationDismissed() {
        if (!LocationRepo.getInstance(getContext()).isLocationEnabled()){
            LocationBottomSheet locationBottomSheet=new LocationBottomSheet(this);
            locationBottomSheet.show(getChildFragmentManager(),"");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        observeQrCode();
        CameraUtil.scannerAlive=true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        CameraUtil.scannerAlive=false;
    }
}

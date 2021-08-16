package com.malcoo.malcotask1.Repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.malcoo.malcotask1.Model.DirectionResponse;
import com.malcoo.malcotask1.Model.Result;
import com.malcoo.malcotask1.R;
import com.malcoo.malcotask1.Utils.MapUtil;
import com.malcoo.malcotask1.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionsRepo {

    private static final String TAG = "DirectionsRepo";
    private  static DirectionsRepo directionsRepo;
    private MutableLiveData<Result<DirectionResponse>> directions;

    private DirectionsRepo() {
    }

    public static DirectionsRepo getInstance() {
        return directionsRepo==null?directionsRepo=new DirectionsRepo():directionsRepo;
    }

    public MutableLiveData<Result<DirectionResponse>> getDirections(Context context, LatLng origin, LatLng dest){
        directions=new MutableLiveData<>();
        String DIRECTIONS_API_KEY=context.getResources().getString(R.string.directions_key);
        RetrofitClient.getInstance()
                .getApi()
                .getDirections(MapUtil.fromCoordinates(origin), MapUtil.fromCoordinates(dest),DIRECTIONS_API_KEY)
                .enqueue(new Callback<DirectionResponse>() {
                    @Override
                    public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                        Log.d(TAG, "onResponse: "+response.body());
                        DirectionResponse directionResponse=response.body();
                        if (response.isSuccessful()&&directionResponse!=null){
                            directions.setValue(Result.SUCCESS(directionResponse));
                        }else {
                            directions.setValue(Result.ERROR("error getting directions"));
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                        directions.setValue(Result.ERROR("error getting directions"));

                    }
                });
        return  directions;
    }

}

package com.malcoo.malcotask1.network;

import com.malcoo.malcotask1.Model.DirectionResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("maps/api/directions/json")
    Call<DirectionResponse> getDirections( @Query( value = "origin",encoded = true) String origin ,
                                                    @Query(value = "destination",encoded = true) String dest, @Query("key") String key);

}

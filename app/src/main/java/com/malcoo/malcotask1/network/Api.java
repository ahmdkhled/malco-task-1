package com.malcoo.malcotask1.network;

import com.malcoo.malcotask1.Model.DirectionResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("")
    Response<DirectionResponse> getDirections(@Query("origin") String origin, @Query("destination") String dest);

}

package com.malcoo.malcotask1.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String DIRECTIONS_BASE_URL="https://maps.googleapis.com/";

    private static RetrofitClient retrofitClient;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance(){
        return retrofitClient==null?retrofitClient=new RetrofitClient():retrofitClient;
    }

    private Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(DIRECTIONS_BASE_URL)
                .addConverterFactory( GsonConverterFactory.create())
                .client(getInterceptorClient())
                .build();

    }

    public Api getApi(){
        return getRetrofit().create(Api.class);
    }

    private OkHttpClient getInterceptorClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }


}

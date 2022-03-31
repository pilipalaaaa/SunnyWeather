package com.sunnyweather.android.logic.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator {
    private static final String BASE_URL = "https://api.caiyunapp.com/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static PlaceService PCreate(Class<PlaceService> placeServiceClass){
        PlaceService placeService = retrofit.create(placeServiceClass);
        return placeService;
    }
    public static WeatherService WCreate(Class<WeatherService> weatherServiceClass){
        WeatherService weatherService = retrofit.create(weatherServiceClass);
        return weatherService;
    }

}

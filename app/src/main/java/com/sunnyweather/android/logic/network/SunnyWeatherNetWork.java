package com.sunnyweather.android.logic.network;


import android.util.Log;

import com.sunnyweather.android.logic.model.PlaceResponse;

import java.io.IOException;

import retrofit2.Response;

public class SunnyWeatherNetWork {
    private static PlaceService placeService =  ServiceCreator.create(PlaceService.class);//完成了retrofit的创建
    private static PlaceResponse Presult = new PlaceResponse();

    public PlaceResponse searchPlaces(String query){
        try {
            Response<PlaceResponse> response = placeService.serarchPlaces(query).execute();
            PlaceResponse body = response.body();
            if(body!=null){
                Presult = body;
                Log.d("SunnyWeatherNetWork","response body is not null");
                Log.d("SunnyWeatherNetWork","status is " +Presult.getStatus() );
            }else {
                Log.d("SunnyWeatherNetWork", "response body is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return Presult;
        }
    }
}

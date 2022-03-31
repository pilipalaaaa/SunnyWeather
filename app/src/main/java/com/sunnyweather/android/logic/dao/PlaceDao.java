package com.sunnyweather.android.logic.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.sunnyweather.android.SunnyWeatherApplication;
import com.sunnyweather.android.logic.model.PlaceResponse;

public class PlaceDao {
    private static SharedPreferences sharedPreferences = SunnyWeatherApplication.getContext()
                                                            .getSharedPreferences("placeData", Context.MODE_PRIVATE);

    public static void savePlace(PlaceResponse.Place place){
        Log.d("Test","调用了存储方法");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("place",gson.toJson(place));
        editor.apply();
    }
    public static PlaceResponse.Place getSavedPlace(){
        Gson gson = new Gson();
        Log.d("Test","调用了读取方法");
        return gson.fromJson(sharedPreferences.getString("place",""),PlaceResponse.Place.class);
    }

    public static Boolean isPlaceSaved(){
        return sharedPreferences.contains("place");
    }
}

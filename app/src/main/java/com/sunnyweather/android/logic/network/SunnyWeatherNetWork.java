package com.sunnyweather.android.logic.network;


import android.util.Log;

import com.sunnyweather.android.logic.model.DailyResponse;
import com.sunnyweather.android.logic.model.PlaceResponse;
import com.sunnyweather.android.logic.model.RealtimeResponse;
import com.sunnyweather.android.logic.model.Weather;

import java.io.IOException;

import retrofit2.Response;

public class SunnyWeatherNetWork {
    private static PlaceService placeService =  ServiceCreator.PCreate(PlaceService.class);//完成了retrofit的创建
    private static PlaceResponse Presult = new PlaceResponse();

    //获取返回信息
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
    private static WeatherService weatherService = ServiceCreator.WCreate(WeatherService.class);
    private static RealtimeResponse Rresult = new RealtimeResponse();
    private static DailyResponse Dresult = new DailyResponse();
    public RealtimeResponse getRealtimeWeather(String lng,String lat){

        try {
            Response<RealtimeResponse> response = weatherService.getRealtimeWeather(lng,lat).execute();
            if(response.body()!=null){
                Rresult = response.body();
                Log.d("SunnyWeatherNetWork1","status is " +Rresult.getStatus() );
                Log.d("SunnyWeatherNetWork1","Realtime response body is not null");
            }else {
                Log.d("SunnyWeatherNetWork1","Realtime response body is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return Rresult;
        }
    }
    //获取未来天气数据
    public DailyResponse getDailyWeather(String lng,String lat) {
        Log.d("SunnyWeatherNetWork", "查看传入的lng和lat" + lng + " " + lat);

        try {
            Response<DailyResponse> response = weatherService.getDailyWeather(lng, lat).execute();
            if (response.body() != null) {
                Dresult = response.body();
                Log.d("SunnyWeatherNetWork2", "Dailytime response body is not null");
                Log.d("SunnyWeatherNetWork2", "Dresult is " + Dresult.getResult());
            } else {
                Log.d("SunnyWeatherNetWork2", "Daily response body is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return Dresult;
        }
    }
}

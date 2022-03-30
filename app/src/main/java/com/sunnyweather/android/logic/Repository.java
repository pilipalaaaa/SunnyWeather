package com.sunnyweather.android.logic;

import static java.lang.Thread.sleep;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.sunnyweather.android.logic.model.PlaceResponse;
import com.sunnyweather.android.logic.network.SunnyWeatherNetWork;
import java.util.List;

public class Repository {
    //@Description 仓库类，封装了许多数据获取操作，提供数据给ViewModel层，同时接收ViewModel层的数据请求
    private static MutableLiveData<List<PlaceResponse.Place>> placesData = new MutableLiveData<>();
    final static SunnyWeatherNetWork sunnyWeatherNetwork = new SunnyWeatherNetWork();
    private static List<PlaceResponse.Place> places;

    public MutableLiveData<List<PlaceResponse.Place>> searchPlaces(String query){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /*获取到搜索到的数据，在network层已经进行判空处理，保证了接收到的placeResponse不为null
                     * 但由于网络请求的同步问题，每次发出的第一个请求的结果，会导致placeResponse为 null，
                     * 因此在PlaceResponse中，需要new 一个 status和result，避免空指针异常。
                     *
                     * */
                    final PlaceResponse placeResponse = sunnyWeatherNetwork.searchPlaces(query);

                    if (placeResponse.getStatus().equals("ok")) {//如果状态ok了，一般来说places就不会有空指针异常
                        places = placeResponse.getPlaces();// 获取到包含地区信息的list
                        Log.d("Repository","place response success " );
                        placesData.postValue(places);//将list传入Livedata内，并准备返回
                    } else {
                        //返回状态不是ok的情况
                        Log.d("Repository", "place status is" + placeResponse.getStatus() );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Repository","PlaceResponse Error!");
                } finally {
                    Log.d("Repository","PlaceResponse finish!");
                }
            }
        }).start();
        return placesData;//返回livedata
    }
}

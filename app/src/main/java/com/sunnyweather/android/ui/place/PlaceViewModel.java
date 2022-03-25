package com.sunnyweather.android.ui.place;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.sunnyweather.android.logic.Repository;
import com.sunnyweather.android.logic.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceViewModel extends ViewModel {
    private static MutableLiveData<String> searchLiveData = new MutableLiveData<>();
    private  static List<Place> placeList = new ArrayList<>();
    private Repository repository = new Repository() ;
    public final LiveData<List<Place>> placeLiveData = Transformations.switchMap(searchLiveData,
            query -> repository.searchPlaces(query));
    public void searchPlace(String query){
        Log.d("PlaceViewModel","query is " + query);
        searchLiveData.postValue(query);
    }
    public void listClear(){
        placeList.clear();
    }
    public static List<Place> getPlaceList() {
        return placeList;
    }
    public void addAllList(List<Place> places){
        placeList.addAll(places);
    }
}

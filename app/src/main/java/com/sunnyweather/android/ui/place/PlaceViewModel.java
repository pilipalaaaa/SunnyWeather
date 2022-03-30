package com.sunnyweather.android.ui.place;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.sunnyweather.android.logic.Repository;
import com.sunnyweather.android.logic.model.PlaceResponse;

import java.util.ArrayList;
import java.util.List;

public class PlaceViewModel extends ViewModel {
    private  static MutableLiveData<String> searchLiveData =
            new MutableLiveData<>();
    private Repository repository = new Repository() ;

    private  static List<PlaceResponse.Place> placeList = new ArrayList<>();

    public final LiveData<List<PlaceResponse.Place>> placeLiveData = Transformations.switchMap(searchLiveData,
            query -> repository.searchPlaces(query));

    //此方法只要被调用，就会执行上面的switchmap
    public void searchPlace(String query){
        Log.d("PlaceViewModel","query is " + query);
        searchLiveData.postValue(query);
    }
    public void listClear(){
        placeList.clear();
    }
    public List<PlaceResponse.Place> getPlaceList() {
        return placeList;
    }
    public void addAllList(List<PlaceResponse.Place> places){
        placeList.addAll(places);
    }
}

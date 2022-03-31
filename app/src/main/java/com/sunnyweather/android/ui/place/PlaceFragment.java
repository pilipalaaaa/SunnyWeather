package com.sunnyweather.android.ui.place;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnyweather.android.MainActivity;
import com.sunnyweather.android.R;
import com.sunnyweather.android.WeatherActivity;
import com.sunnyweather.android.logic.model.PlaceResponse;
import com.sunnyweather.android.logic.model.Weather;

import java.util.List;

public class PlaceFragment extends Fragment {
    private static PlaceViewModel viewModel;
    private PlaceAdapter adapter;

    public static PlaceViewModel getViewModel() {
        return viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        return inflater.inflate(R.layout.fragment_place,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getActivity().getClass().equals(MainActivity.class)&&viewModel.isPlaceSaved()){
            Log.d("Test","进入读取存储的模式");
            PlaceResponse.Place place = viewModel.getSavedPlace();
            Intent intent = new Intent(getContext(), WeatherActivity.class);
            intent.putExtra("location_lng",place.getLocation().getLng());
            intent.putExtra("location_lat",place.getLocation().getLat());
            intent.putExtra("place_name",place.getName());

            startActivity(intent);
            getActivity().finish();
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        PlaceAdapter adapter = new PlaceAdapter(this,viewModel.getPlaceList());
        recyclerView.setAdapter(adapter);

        ImageView bgImageView = getActivity().findViewById(R.id.byImageView);
        EditText searchPlaceEdit = getActivity().findViewById(R.id.searchPlaceEdit);
        searchPlaceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = s.toString();
                Log.d("PlaceFragment", "query is "+content);
                if(content.isEmpty()){
                    recyclerView.setVisibility(View.GONE);

                    bgImageView.setVisibility(View.VISIBLE);
                    viewModel.listClear();
                    adapter.notifyDataSetChanged();
                }
                else {
                    viewModel.searchPlace(content);
                    //测试下面是这里未能获取到livedata

                    Log.d("PlaceFragment","" + viewModel.placeLiveData.getValue());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //转换后的livedata观察数据变化
        viewModel.placeLiveData.observe(getViewLifecycleOwner(), new Observer<List<PlaceResponse.Place>>() {
            @Override
            public void onChanged(List<PlaceResponse.Place> placeList) {
                if(placeList != null){
                    //Log.d("PlaceFragment","placeList in final LiveData is "+ placeList.get(0));
                    Log.d("PlaceFragment","数据已变化");
                    recyclerView.setVisibility(View.VISIBLE);
                    bgImageView.setVisibility(View.GONE);
                    viewModel.listClear();
                    viewModel.addAllList(placeList);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Log.d("PlaceFragment","数据空");
                    Toast.makeText(getActivity(),"未能查询到任何地点", Toast.LENGTH_SHORT).show();
                    //错误处理
                }
            }
        });
    }
}

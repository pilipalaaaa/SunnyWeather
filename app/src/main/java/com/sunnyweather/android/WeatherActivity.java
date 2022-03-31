package com.sunnyweather.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunnyweather.android.R;
import com.sunnyweather.android.logic.model.DailyResponse;
import com.sunnyweather.android.logic.model.RealtimeResponse;
import com.sunnyweather.android.logic.model.Sky;
import com.sunnyweather.android.logic.model.Weather;
import com.sunnyweather.android.ui.weather.WeatherViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {
    private static WeatherViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawerLayout;

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public static WeatherViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏和背景融合
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        //获取viewmodel实例
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

       if(viewModel.locationLng.isEmpty()){
           if (getIntent().getStringExtra("location_lng")==null){
               viewModel.locationLng = "";
           }else{
               viewModel.locationLng = getIntent().getStringExtra("location_lng");
           }
       }
        if(viewModel.locationLat.isEmpty()){
            if(getIntent().getStringExtra("location_lat") == null){
                viewModel.locationLat = "";

            }
            else {
                viewModel.locationLat = getIntent().getStringExtra("location_lat");
                Log.d("WeatherActivity","lat is " + viewModel.locationLat);
            }
        }

        if(viewModel.placeName.isEmpty()){
            if(getIntent().getStringExtra("place_name") == null){
                viewModel.placeName = "";
            }
            else {
                viewModel.placeName = getIntent().getStringExtra("place_name");
            }
        }
        viewModel.weatherLiveData.observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if(weather != null){
                    Log.d("WeatherActivity","数据已经变化");
                    showWeatherInfo(weather);
                }
                else {
                    Toast.makeText(getApplicationContext(),"获取天气信息失败",Toast.LENGTH_SHORT).show();;
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshWeather();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWeather();
            }
        });
        //数据丢进去引起switchmap
//        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat);

        //*****************************************************************************
        //*****************************************************************************
        //下面是滑动菜单切换城市的逻辑
        Button navBtn = findViewById(R.id.navBtn);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                InputMethodManager manager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(drawerView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                //滑动菜单隐藏时隐藏键盘
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    //封装的刷新天气信息的逻辑
    public void refreshWeather(){
        //传入设置好的经纬度
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat);
        swipeRefreshLayout.setRefreshing(true);
    }

    //将viewmodel数据显示在view界面上
    private void showWeatherInfo(Weather weather){
        TextView placeName = findViewById(R.id.placeName);
        placeName.setText(viewModel.placeName);

        RealtimeResponse.Realtime realtime = weather.getRealtime();
        DailyResponse.Daily daily = weather.getDaily();
        Log.d("WeatherInfo","daily data is " + daily.getTemperature().get(0));

        //填充now.xml
        TextView currentTempText = findViewById(R.id.currentTemp);
        Log.d("WeatherActivity","Temperature is " + realtime.getTemperature());
        currentTempText.setText((int) realtime.getTemperature().floatValue() + "℃");

        TextView currentSky = findViewById(R.id.currentSky);
        currentSky.setText(Sky.getSky(realtime.getSkycon()).getInfo());

        TextView currentAQIText = findViewById(R.id.currentAQI);
        currentAQIText.setText("空气指数" +
                (int)realtime.getAirQuality().getAqi().getChn().floatValue());

        RelativeLayout nowLayout = findViewById(R.id.nowLayout);
        nowLayout.setBackgroundResource(Sky.getSky(realtime.getSkycon()).getBg());

        //填充forecast.xml的数据
        LinearLayout forecastLayout = findViewById(R.id.forecastLayout);
        forecastLayout.removeAllViews();
        int days = daily.getSkycon().size();

        for(int i = 0;i < days;i++) {//这里是kotlin中的for in 循环
            DailyResponse.Skycon skycon = daily.getSkycon().get(i);
            DailyResponse.Temperature temperature = daily.getTemperature().get(i);

            View view = LayoutInflater.from(this).
                    inflate(R.layout.forecast_item, forecastLayout, false);

            TextView dataInfo = view.findViewById(R.id.dateInfo);
            ImageView skyIcon = view.findViewById(R.id.skyIcon);
            TextView skyInfo = view.findViewById(R.id.skyInfo);
            TextView temperatureInfo = view.findViewById(R.id.temperatureInfo);

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            dataInfo.setText(simpleDateFormat.format(skycon.getDate()));
            Sky sky = Sky.getSky(skycon.getValue());
            skyIcon.setImageResource(sky.getIcon());
            skyInfo.setText(sky.getInfo());
            temperatureInfo.setText((int) temperature.getMin().floatValue() + "~" +
                    (int) temperature.getMax().floatValue());

            forecastLayout.addView(view);
        }
        //填充life_index.xml 布局中的数据
        DailyResponse.LifeIndex lifeIndex = daily.getLifeIndex();
        TextView coldRiskText = findViewById(R.id.coldRiskText);
        coldRiskText.setText(lifeIndex.getColdRisk().get(0).getDesc());

        TextView dressingText = findViewById(R.id.dressingText);
        dressingText.setText(lifeIndex.getDressing().get(0).getDesc());

        TextView ultravioletText = findViewById(R.id.ultravioletText);
        ultravioletText.setText(lifeIndex.getUltraviolet().get(0).getDesc());

        TextView carWashingText = findViewById(R.id.carWashingText);
        carWashingText.setText(lifeIndex.getCarWashing().get(0).getDesc());

        ScrollView weatherLayout = findViewById(R.id.weatherLayout);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
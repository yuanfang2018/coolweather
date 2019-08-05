package com.coolweather.android;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.coolweather.android.db.Country;
import com.coolweather.android.db.CountryOwned;
import com.coolweather.android.db.WeatherType;
import com.coolweather.android.gson.Alarm;
import com.coolweather.android.gson.EveryHour;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Suggestion;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView aqiText;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private ScrollView weatherLayout;
    private LinearLayout weatherLinearLayout;
    private WeatherChartView mCharView;
    private LinearLayout hoursLayout;

    private List<CountryOwned> countryOwnedList;

    private LocationClient mLocationClient;

    public SwipeRefreshLayout swipeRefresh;
    private ArrayAdapter<String> adapter;
    private Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_weather);
        //初始化各控件
        //swipeRefresh.setColorSchemeResources(R.color.colorFontDark);

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String weatherString = prefs.getString("weather", null);null

//        if (weatherString != null) {
//            //有缓存时直接解析天气数据
//            Weather weather = Utility.handleWeatherResponse(weatherString);
//            mWeatherId = weather.city.citykey;
//            showWeatherInfo(weather);
//        } else {
//            //无缓存时去服务器查询天气
//            weatherId = getIntent().getStringExtra("weather_id");
//            weatherLayout.setVisibility(View.INVISIBLE);
//            requestWeather(weatherId);
//        }
        initView();
        requestPermissions();
        weatherLayout.setVisibility(View.INVISIBLE);
        countryOwnedList = DataSupport.findAll(CountryOwned.class);
        if(countryOwnedList.size() != 0){
            requestWeather(countryOwnedList.get(0).getWeatherId());
            getIntent().putExtra("weather_id", countryOwnedList.get(0).getWeatherId());
        }

        setSupportActionBar(toolbar);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String weatherId = getIntent().getStringExtra("weather_id");
                if(weatherId == null){
                    CountryOwned localCity = DataSupport.where("level = ?", "2").findFirst(CountryOwned.class);
                    weatherId = localCity.getWeatherId();
                    getIntent().putExtra("weather_id", weatherId);
                }
                requestWeather(weatherId);
            }
        });
//        titleUpdateTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(WeatherActivity.this, ManageCitiesActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://zhwnlapi.etouch.cn/Ecalender/api/v2/weather?date=20180324&citykey=" + weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.city.status == 1000) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){
        String cityName = weather.city.cityName;
        //String updateTima = weather.update.localDate;
        String degree = weather.now.temperature + "°";
        String weatherInfo = weather.now.weatherType;
        int aqi = weather.enviroment.aqi;
        //处理天气类型与天气类型代码对应关系
        Utility.handleWeatherType(weather);
        //设置UI
        toolbar.setTitle(weather.city.cityName);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        aqiText.setText(String.valueOf(aqi) + " " + weather.enviroment.quality);
        //设置温度变化曲线
        initWeatherLine(weather);

        //预警
        alarm = weather.alarm;
        invalidateOptionsMenu();

        aqiText.setBackgroundResource(R.drawable.corner_view);
        GradientDrawable drawable =(GradientDrawable)aqiText.getBackground();
        switch (weather.enviroment.quality){
            case "优":
                drawable.setColor(getResources().getColor(R.color.优));
                break;
            case "良":
                drawable.setColor(getResources().getColor(R.color.良));
                break;
            case "轻度污染":
                drawable.setColor(getResources().getColor(R.color.轻度污染));
                break;
            case "中度污染":
                drawable.setColor(getResources().getColor(R.color.中度污染));
                break;
            case "重度污染":
                drawable.setColor(getResources().getColor(R.color.重度污染));
                break;
            case "严重污染":
                drawable.setColor(getResources().getColor(R.color.严重污染));
                break;
            default:
                drawable.setColor(getResources().getColor(R.color.严重污染));
        }

        //添加最近六天天气情况信息
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(getWeek(forecast.date));
            infoText.setText(forecast.day.weatherType);
            maxText.setText(String.valueOf(forecast.highWendu) + "°");
            minText.setText(String.valueOf(forecast.lowWendu) + "°");
            forecastLayout.addView(view);
        }

        //未来24小时天气
        hoursLayout.removeAllViews();
        for (EveryHour everyHour:weather.everyHourList) {
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.hours_item, hoursLayout, false);
            TextView hourstimeText = view.findViewById(R.id.tv_time);
            TextView hoursdegreeText = view.findViewById(R.id.tv_degree);
            TextView hourstypeText = view.findViewById(R.id.tv_type);
            hourstimeText.setText(everyHour.time.substring(8, 10)+ ":00");
            hoursdegreeText.setText(String.valueOf(everyHour.degree) + "°");
            WeatherType weatherType = DataSupport.where("type = ?", String.valueOf(everyHour.type)).findFirst(WeatherType.class);
            hourstypeText.setText(weatherType.getWthr());
            hoursLayout.addView(view);
        }

        //生活指数
        List<Suggestion> suggestions = new ArrayList<>();
        suggestions.add(weather.suggestionList.get(0));
        suggestions.add(weather.suggestionList.get(1));
        suggestions.add(weather.suggestionList.get(3));
        suggestions.add(weather.suggestionList.get(4));
        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.chenlian);
        images.add(R.mipmap.chuanyi);
        images.add(R.mipmap.ganmao);
        images.add(R.mipmap.ziwaixian);
        weatherLinearLayout.removeAllViews();
        for (int i = 0; i < suggestions.size(); i++) {
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.suggestion_item, weatherLinearLayout, false);
            ImageView sugImage = view.findViewById(R.id.suggestion_iv);
            TextView sugTitle = view.findViewById(R.id.sug_title_text);
            TextView sugdes = view.findViewById(R.id.sug_des_text);
            sugImage.setImageResource(images.get(i));
            sugTitle.setText(suggestions.get(i).suggestionName + "  " + suggestions.get(i).suggestionValue);
            sugdes.setText(suggestions.get(i).suggestionDescribe);
            weatherLinearLayout.addView(view);
        }

        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void initWeatherLine(Weather weather){
        // set day
        int[] degreesH = new int[6];
        for(int i = 0; i < weather.forecastList.size(); i++){
            degreesH[i] = weather.forecastList.get(i).highWendu;
        }
        mCharView.setTempDay(degreesH);
        // set night
        int[] degreesL = new int[6];
        for(int i = 0; i < weather.forecastList.size(); i++){
            degreesL[i] = weather.forecastList.get(i).lowWendu;
        }
        mCharView.setTempNight(degreesL);
        mCharView.invalidate();
    }

    public String getWeek(String pTime) {

        String Week = "周";

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        Calendar calendar = Calendar.getInstance();
        String pp = calendar.getTime().toString();
        if (c.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            return "今天";
        }
        if(c.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) - 1
                || ((c.get(Calendar.DAY_OF_MONTH) == 30 || c.get(Calendar.DAY_OF_MONTH) == 31) && calendar.get(Calendar.DAY_OF_MONTH) == 1)){
            return "昨天";
        }
        return Week;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(alarm == null) {
            menu.findItem(R.id.notification).setVisible(false);
        }else{
            menu.findItem(R.id.notification).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.about:
                Intent intent = new Intent(WeatherActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.notification:
                Intent intent1 = new Intent(WeatherActivity.this, NotificationActivity.class);
                intent1.putExtra("noti_title", alarm.type+alarm.degree);
                intent1.putExtra("noti_icon", alarm.icon);
                intent1.putExtra("noti_desc", alarm.desc);
                startActivity(intent1);
                break;
            default:
        }
        return true;
    }

    private void initView() {
        //titleUpdateTime = findViewById(R.id.title_update_time);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.weather_toolbar);
        degreeText = findViewById(R.id.tv_degree);
        weatherInfoText = findViewById(R.id.tv_weatherType);
        aqiText = findViewById(R.id.tv_aqi);
        mCharView = findViewById(R.id.line_chart);
        forecastLayout = findViewById(R.id.forecast_layout);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorFontDark);
        hoursLayout = findViewById(R.id.linearLayout_hours);
        weatherLayout = findViewById(R.id.weather_layout);
        weatherLinearLayout = findViewById(R.id.weather_linearlayout);

        toolbar.setNavigationIcon(R.mipmap.ic_menu_black_24dp);
        toolbar.setOverflowIcon(getDrawable(R.mipmap.ic_more_vert_black_24dp));
    }

    /**
     * 请求定位权限
     */
    public void requestPermissions(){
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(WeatherActivity.this, permissions, 1);
        }else{
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String adCode = bdLocation.getAdCode();
                    Country country = DataSupport.where("adCode = ?", adCode).find(Country.class).get(0);
                    Utility.handleLocalCityResponse(country);
                }
            });
        }
    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
//        option.setEnableSimulateGps(true);
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        if(mLocationClient != null){
            mLocationClient.stop();
        }
        super.onDestroy();
    }

    /**
     * 点击返回键两次退出程序
     */
    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else{
                finish();
            }
        }
    }



    @Override
    protected void onResume() {
        String weatherId = getIntent().getStringExtra("weather_id");
        countryOwnedList.clear();
        countryOwnedList.addAll(DataSupport.findAll(CountryOwned.class));
        if(weatherId != null && countryOwnedList.size() > 0){
            CountryOwned countryOwned = DataSupport.where("weatherId = ?", weatherId).findFirst(CountryOwned.class);
            if(countryOwned == null){
                swipeRefresh.setRefreshing(true);
                requestWeather(countryOwnedList.get(0).getWeatherId());
                getIntent().putExtra("weather_id", countryOwnedList.get(0).getWeatherId());
            }
        }
        super.onResume();
    }
}



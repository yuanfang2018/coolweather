package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.db.Country;
import com.coolweather.android.db.CountryOwned;
import com.coolweather.android.db.WeatherType;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

/**
 * Created by Yuan Phang on 2018/3/20.
 */

public class Utility {
//    /**
//     * 解析和处理服务器返回的省级数据
//     */
//    public static boolean handleProvinceResponse(String response){
//        if(!TextUtils.isEmpty(response)){
//            try {
//                JSONArray allProvinces = new JSONArray(response);
//                for(int i=0; i<allProvinces.length(); i++) {
//                    JSONObject provinceObject = allProvinces.getJSONObject(i);
//                    Province province = new Province();
//                    province.setProvinceName(provinceObject.getString("name"));
//                    province.setProvinceCode(provinceObject.getInt("id"));
//                    province.save();
//                }
//                return true;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    /**
     *解析和处理服务器返回的市级数据
     */

//    public static boolean handleCityResponse(String response, int provinceId){
//        if(!TextUtils.isEmpty(response)){
//            try {
//                JSONArray allCities = new JSONArray(response);
//                for(int i=0; i<allCities.length(); i++){
//                    JSONObject cityObject = allCities.getJSONObject(i);
//                    City city = new City();
//                    city.setCityName(cityObject.getString("name"));
//                    city.setCityCode(cityObject.getInt("id"));
//                    city.setProvinceId(provinceId);
//                    city.save();
//                }
//                return true;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountryResponse(String response, int cityId){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCountries = new JSONArray(response);
                for(int i=0; i<allCountries.length(); i++){
                    JSONObject countryObject = allCountries.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryName(countryObject.getString("cname"));
                    country.setWeatherId(countryObject.getString("cid").substring(2));
                    country.setCityName(countryObject.getString("cty_name"));
                    country.setProvinceName(countryObject.getString("provc_name"));
                    country.setAdCode(countryObject.getString("adcode"));
                    country.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
//            JSONArray jsonArray = jsonObject.getJSONArray("data");
//            JSONArray jsonArray = new JSONArray(response);
//            String weatherContent = jsonArray.getJSONObject(0).toString();
            String weatherContent = jsonObject.toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean handleCountryOwnedResponse(Country country){
        CountryOwned countryOwned = new CountryOwned();
        countryOwned.setWeatherId(country.getWeatherId());
        countryOwned.setAdCode(country.getAdCode());
        countryOwned.setCityName(country.getCityName());
        countryOwned.setProvinceName(country.getProvinceName());
        countryOwned.setCountryName(country.getCountryName());
        if(DataSupport.where("weatherId = ?", country.getWeatherId()).find(CountryOwned.class).size() == 0){
            countryOwned.save();
            return true;
        }else{
            return false;
        }
    }

    public static void handleLocalCityResponse(Country country) {
        CountryOwned localCity = DataSupport.where("level = ?", "2").findFirst(CountryOwned.class);
        if(localCity == null) localCity = new CountryOwned();
        localCity.setWeatherId(country.getWeatherId());
        localCity.setAdCode(country.getAdCode());
        localCity.setCityName(country.getCityName());
        localCity.setProvinceName(country.getProvinceName());
        localCity.setCountryName(country.getCountryName());
        localCity.setLevel(2);
        localCity.save();

//        if (DataSupport.where("weatherId = ?", country.getWeatherId()).find(CountryOwned.class).size() == 0) {
//
//            return true;
//        } else {
//            return false;
//        }
    }

    public static boolean handleWeatherType(Weather weather){
        if(weather == null){
            return false;
        }
        for (Forecast forecast:weather.forecastList) {
            WeatherType weatherType = new WeatherType();
            weatherType.setType(forecast.day.weatherTypeInt);
            weatherType.setWthr(forecast.day.weatherType);
            weatherType.save();
            weatherType.setType(forecast.night.weatherTypeInt);
            weatherType.setWthr(forecast.night.weatherType);
            weatherType.save();
        }
        return true;
    }
}

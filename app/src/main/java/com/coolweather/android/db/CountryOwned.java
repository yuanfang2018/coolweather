package com.coolweather.android.db;

import com.coolweather.android.gson.Weather;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class CountryOwned extends DataSupport {

    @Column(defaultValue = "0")
    private int level;

    private int id;

    @SuppressWarnings("cname")
    private String countryName;

    @Column(unique = true)
    @SuppressWarnings("cid")
    private String weatherId;

    @SuppressWarnings("provc_name")
    private String provinceName;

    @SuppressWarnings("cty_name")
    private String cityName;

    private String adCode;

//    private Weather weatherInfo;
//
//    public Weather getWeatherInfo() {
//        return weatherInfo;
//    }
//
//    public void setWeatherInfo(Weather weatherInfo) {
//        this.weatherInfo = weatherInfo;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Yuan Phang on 2018/3/20.
 */

public class Country extends DataSupport {
    private int id;

    @SuppressWarnings("cname")
    private String countryName;

    @SuppressWarnings("cid")
    private String weatherId;

    @SuppressWarnings("provc_name")
    private String provinceName;

    @SuppressWarnings("cty_name")
    private String cityName;

    private String adCode;

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

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
}

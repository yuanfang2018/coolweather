package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yuan Phang on 2018/3/22.
 */

public class Data {

    public Yesterday yesterday;

    @SerializedName("city")
    public String cityName;

    @SerializedName("forecast")
    public List<Forecast> forecastList;

    public String ganmao;

    public String wendu;

}

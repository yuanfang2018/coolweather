package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yuan Phang on 2018/3/22.
 */

public class Weather {
    @SerializedName("indexes")
    public List<Suggestion> suggestionList;

    @SerializedName("meta")
    public City city;

    @SerializedName("forecast")
    public List<Forecast> forecastList;

    @SerializedName("hourfc")
    public List<EveryHour> everyHourList;

    @SerializedName("evn")
    public Enviroment enviroment;

    @SerializedName("observe")
    public Now now;
}

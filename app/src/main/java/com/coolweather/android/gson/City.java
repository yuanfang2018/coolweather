package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuan Phang on 2018/3/24.
 */

public class City {
    public String citykey;

    @SerializedName("city")
    public String cityName;

    @SerializedName("upper")
    public String upperCityName;

    @SerializedName("up_time")
    public String updateTime;

    public int status;
}

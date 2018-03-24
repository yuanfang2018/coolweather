package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuan Phang on 2018/3/24.
 */

public class Now {

    public String shidu;

    @SerializedName("wthr")
    public String weatherType;

    @SerializedName("temp")
    public int temperature;

    @SerializedName("up_time")
    public String updateTime;

    @SerializedName("wp")
    public String windSpeed;

    public String tigan;

    @SerializedName("type")
    public int weatherTypeInt;

    @SerializedName("wd")
    public String windDiretion;

}

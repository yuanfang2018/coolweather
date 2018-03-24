package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuan Phang on 2018/3/24.
 */

public class EveryHour {
    @SerializedName("wthr")
    public int degree;

    public String shidu;

    @SerializedName("wp")
    public String windSpeed;

    public String time;

    public int type;

    @SerializedName("wd")
    public String windDirection;
}

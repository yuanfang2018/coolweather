package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuan Phang on 2018/3/22.
 */

public class Forecast {

    public String date;

    public String sunrise;

    @SerializedName("high")
    public int highWendu;

    @SerializedName("low")
    public int lowWendu;

    public String sunset;

    public DayAndNight night;

    public int aqi;

    public DayAndNight day;

    public class DayAndNight{
        @SerializedName("wthr")
        public String weatherType;

        @SerializedName("wp")
        public String windSpeed;

        @SerializedName("type")
        public int weatherTypeInt;

        @SerializedName("wd")
        public String windDirection;

        public String notice;
    }

}

package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuan Phang on 2018/3/22.
 */

public class Forecast {

    public String date;

    @SerializedName("high")
    public String highWendu;

    @SerializedName("low")
    public String lowWendu;

    public String fenli;

    public String fengxiang;

    @SerializedName("type")
    public String weatherType;

}

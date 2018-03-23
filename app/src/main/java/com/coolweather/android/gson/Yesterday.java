package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuan Phang on 2018/3/22.
 */

public class Yesterday {
    public String date;

    @SerializedName("high")
    public String highWendu;

    @SerializedName("low")
    public String lowWendu;

    @SerializedName("fx")
    public String fengxiang;

    @SerializedName("fl")
    public String fengli;

    @SerializedName("type")
    public String weatherType;
}

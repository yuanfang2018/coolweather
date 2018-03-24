package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuan Phang on 2018/3/24.
 */

public class Suggestion {
    @SerializedName("name")
    public String suggestionName;

    @SerializedName("value")
    public String suggestionValue;

    @SerializedName("desc")
    public String suggestionDescribe;
}

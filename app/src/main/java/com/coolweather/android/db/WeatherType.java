package com.coolweather.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class WeatherType extends DataSupport{

    private int id;

    @Column(unique = true)
    private int type;

    private String wthr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWthr() {
        return wthr;
    }

    public void setWthr(String wthr) {
        this.wthr = wthr;
    }
}

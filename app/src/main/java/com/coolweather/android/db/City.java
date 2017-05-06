package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by asus on 2017/5/7.
 */

public class City extends DataSupport {
    private String cityName;
    private int cityCode;
    private int provinceId;

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }
}

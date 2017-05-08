package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2017/5/7.
 */

public class Basic  {

    /**
     * city : 青岛
     * id : CN101120201
     * update : {"loc":"2016-08-30 11:52"}
     */

    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weather;
    @SerializedName("update")
    public Update update;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public class Update {
        /**
         * loc : 2016-08-30 11:52
         */

        @SerializedName("loc")
       public String updateTime;

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}

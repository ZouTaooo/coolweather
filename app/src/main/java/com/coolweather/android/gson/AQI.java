package com.coolweather.android.gson;

/**
 * Created by asus on 2017/5/7.
 */

public class AQI {

    /**
     * city : {"aqi":"60","pm25":"15"}
     */

    public AQICity city;

    public AQICity getCity() {
        return city;
    }

    public void setCity(AQICity city) {
        this.city = city;
    }

    public static class AQICity {
        /**
         * aqi : 60
         * pm25 : 15
         */

        public String aqi;
        public String pm25;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }
    }
}

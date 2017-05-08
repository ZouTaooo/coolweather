package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2017/5/7.
 */

public class Now {

    /**
     * cond : {"code":"100","txt":"晴"}
     * fl : 28
     * hum : 41
     * pcpn : 0
     * pres : 1005
     * tmp : 26
     * vis : 10
     * wind : {"deg":"330","dir":"西北风","sc":"6-7","spd":"34"}
     */

    @com.google.gson.annotations.SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public static class More {
        /**
         * code : 100
         * txt : 晴
         */

        @com.google.gson.annotations.SerializedName("txt")
        public String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}

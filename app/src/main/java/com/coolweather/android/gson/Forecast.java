package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2017/5/7.
 */

public class Forecast {


    /**
     * astro : {"mr":"03:09","ms":"17:06","sr":"05:28","ss":"18:29"}
     * cond : {"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"}
     * date : 2016-08-30
     * hum : 45
     * pcpn : 0.0
     * pop : 8
     * pres : 1005
     * tmp : {"max":"29","min":"22"}
     * vis : 10
     * wind : {"deg":"339","dir":"北风","sc":"4-5","spd":"24"}
     */

    @com.google.gson.annotations.SerializedName("cond")
    public More more;
    @com.google.gson.annotations.SerializedName("date")
    public String date;
    @com.google.gson.annotations.SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{
        public String max;
        public String min;
    }
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}

package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2017/5/7.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public static class Comfort {
        /**
         * brf : 较不舒适
         * txt : 白天天气多云，同时会感到有些热，不很舒适。
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

    public static class CarWash {
        /**
         * brf : 较适宜
         * txt : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
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
    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}

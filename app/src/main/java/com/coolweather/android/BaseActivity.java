package com.coolweather.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/5/18.
 */

public class BaseActivity extends AppCompatActivity {
    List<Activity> activityList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }

    public void finishAll() {
        if (activityList.size()>0){
            for (Activity activity:activityList){
                activity.finish();
            }
        }
    }
}

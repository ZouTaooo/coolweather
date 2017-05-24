package com.coolweather.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.Utility;
import com.suke.widget.SwitchButton;

public class SettingsActivity extends BaseActivity  {

    private Toolbar mToolbar;
    private SwitchButton mSwitchButton;
    private TextView mUpdateSpaceShow;
    private LinearLayout mUpdateSpace;
    private SwitchButton mSwitchButton1;
    private LinearLayout checkUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_settings);

        initView();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    private void initView() {
        checkUpdate = (LinearLayout)findViewById(R.id.check_update);
        mSwitchButton1 = (SwitchButton) findViewById(R.id.switch_button1);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwitchButton = (SwitchButton) findViewById(R.id.switch_button);
        mUpdateSpaceShow = (TextView) findViewById(R.id.update_space_show);
        mUpdateSpace = (LinearLayout) findViewById(R.id.update_space);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUpdateSpaceShow.setText(preferences.getInt("UpdateTime", 6) + "小时");
        mSwitchButton.setChecked(preferences.getBoolean("isChecked", true));
        mSwitchButton1.setChecked(preferences.getBoolean("isNotification",false));
        if (preferences.getBoolean("isChecked", true)) {
            mUpdateSpace.setVisibility(View.VISIBLE);
            Intent intent = new Intent(SettingsActivity.this, AutoUpdateService.class);
            startService(intent);
        } else {
            mUpdateSpace.setVisibility(View.GONE);
        }
        mSwitchButton1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(MyApplication.getContext()).edit();
                editor.putBoolean("isNotification", isChecked);
                editor.apply();
                if (isChecked) {
                    startNotification();
                }else {
                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(1);
                }
            }
        });
        mSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = PreferenceManager.
                            getDefaultSharedPreferences(SettingsActivity.this).edit();
                    editor.putBoolean("isChecked", true);
                    editor.apply();
                    mUpdateSpace.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(SettingsActivity.this, AutoUpdateService.class);
                    startService(intent);
                } else {
                    SharedPreferences.Editor editor = PreferenceManager.
                            getDefaultSharedPreferences(SettingsActivity.this).edit();
                    editor.putBoolean("isChecked", false);
                    editor.apply();
                    mUpdateSpace.setVisibility(View.GONE);
                    Intent intent = new Intent(SettingsActivity.this,AutoUpdateService.class);
                    stopService(intent);
                }
            }
        });
        mUpdateSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setItems(getResources().getStringArray(R.array.TimeArray), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (arg1 == 0) {
                            mUpdateSpaceShow.setText("1小时");
                        } else if (arg1 == 1) {
                            mUpdateSpaceShow.setText("2小时");
                        } else if (arg1 == 2) {
                            mUpdateSpaceShow.setText("3小时");
                        } else if (arg1 == 3) {
                            mUpdateSpaceShow.setText("4小时");
                        } else if (arg1 == 4) {
                            mUpdateSpaceShow.setText("5小时");
                        } else if (arg1 == 5) {
                            mUpdateSpaceShow.setText("6小时");
                        }
                        Toast.makeText(MyApplication.getContext(),"设置成功^_^",Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(SettingsActivity.this).edit();
                        editor.putInt("UpdateTime", arg1 + 1);
                        editor.apply();
                        arg0.dismiss();
                    }
                });
                builder.show();
            }
        });
        checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,"正在获取版本信息",Toast.LENGTH_LONG).show();
                Toast.makeText(SettingsActivity.this,"已经是最新版本^_^",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startNotification() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);
        Weather weather = Utility.handleWeatherResponse(weatherString);
        Intent intent = new Intent(this, WeatherActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(weather.now.temperature+"°  " +weather.now.more.info)
                .setContentText(weather.suggestion.colthes.info)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        manager.notify(1, notification);
    }
}

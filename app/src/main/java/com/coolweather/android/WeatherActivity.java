package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.db.SavedCity;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.coolweather.android.R.id.drawer_layout;
import static com.coolweather.android.R.id.nav_about;
import static com.coolweather.android.R.id.nav_add_city;
import static com.coolweather.android.R.id.nav_city_control;
import static com.coolweather.android.R.id.nav_exit;
import static com.coolweather.android.R.id.nav_settings;


public class WeatherActivity extends BaseActivity {
    private TextView mMaxAndMin;
    private ImageView mBingPic;
    private TextView mTitleCity;
    private TextView mTitleUpdateTime;
    private TextView mDegreeText;
    private TextView mWeatherInfoText;
    private TextView mAqiText;
    private Button mNavButton;
    private TextView mPm25Text;
    private LinearLayout mForecastLayout;
    private TextView mComfortText;
    private TextView mCarWashText;
    private TextView mSportText;
    private ScrollView mWeatherLayout;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    /**
     * @param savedInstanceState
     */
    private static final String TAG = "WeatherActivity";
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            mDrawerLayout.setClipToPadding(false);
        }
        setContentView(R.layout.activity_weather);
        //初始化各控件
        initView();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (getIntent().getStringExtra("weatherId")==null) {
            String weatherString = preferences.getString("weather", null);
            if (weatherString != null) {
                //有缓存时直接解析天气数据
                Log.d("无缓存", "" + weatherString);
                Weather weather = Utility.handleWeatherResponse(weatherString);
                showWeatherInfo(weather);
            }
        }else{
            final String weatherId;
            weatherId = getIntent().getStringExtra("weatherId");
            mWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherString = preferences.getString("weather", null);
                final String weatherId;
                Weather weather = Utility.handleWeatherResponse(weatherString);
                if (weather!=null) {
                weatherId = weather.basic.weather;
                requestWeather(weatherId);
                }
            }
        });
        mNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        String bingPic = preferences.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(mBingPic);
        } else {
            loadBingPic();
        }
    }

    private void initView() {
        mMaxAndMin = (TextView) findViewById(R.id.maxandmin);
        mBingPic = (ImageView) findViewById(R.id.bing_pic_img);
        mNavButton = (Button) findViewById(R.id.nav_button);
        mDrawerLayout = (DrawerLayout) findViewById(drawer_layout);
        mTitleCity = (TextView) findViewById(R.id.title_city);
        mTitleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        mDegreeText = (TextView) findViewById(R.id.degree_text);
        mWeatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        mAqiText = (TextView) findViewById(R.id.aqi_text);
        mPm25Text = (TextView) findViewById(R.id.pm25_text);
        mForecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        mComfortText = (TextView) findViewById(R.id.comfort_text);
        mCarWashText = (TextView) findViewById(R.id.car_wash_text);
        mSportText = (TextView) findViewById(R.id.sport_text);
        mWeatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case nav_add_city:
                        intent = new Intent(WeatherActivity.this, AddActivity.class);
                        startActivity(intent);
                        break;

                    case nav_city_control:
                        if (DataSupport.findAll(SavedCity.class).size()<=0) {
                            Toast.makeText(WeatherActivity.this,"还未添加城市！",Toast.LENGTH_SHORT).show();
                        }else {
                            intent = new Intent(WeatherActivity.this, ControlActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case nav_settings:
                        intent = new Intent(WeatherActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;

                    case nav_about:
                        intent = new Intent(WeatherActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;

                    case nav_exit:
                        finishAll();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //改变上下层的焦点问题
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /*加载必应每日*/
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(mBingPic);
                    }
                });
            }
        });
    }

    /*根据天气id请求城市天气信息*/
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=9a0630bf74da41e181fc786957a28e2c";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
            }
        });
        loadBingPic();
    }

    /*处理并展示Weather实体类中的数据*/
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°";
        String weatherInfo = weather.now.more.info;
        mTitleCity.setText(cityName);
        mTitleUpdateTime.setText(updateTime);
        mDegreeText.setText(degree);
        mWeatherInfoText.setText(weatherInfo);
        mForecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            mForecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            mAqiText.setText(weather.aqi.city.aqi);
            mPm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度:" + weather.suggestion.comfort.info;
        String carWash = "洗车指数:" + weather.suggestion.carWash.info;
        String sport = "运动建议:" + weather.suggestion.sport.info;
        mMaxAndMin.setText(weather.forecastList.get(0).temperature.min + "~" + weather.forecastList.get(0).temperature.max);
        mComfortText.setText(comfort);
        mCarWashText.setText(carWash);
        mSportText.setText(sport);
        mWeatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}

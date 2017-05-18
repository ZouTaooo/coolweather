package com.coolweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.coolweather.android.db.SavedCity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ControlActivity extends BaseActivity {
    private List<String> dataList;
    private List<SavedCity> savedCityList;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "ControlActivity";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        final ListView listView = (ListView) findViewById(R.id.list_saved);
        dataList = new ArrayList<>();
        savedCityList = DataSupport.findAll(SavedCity.class);
        for (SavedCity savedCity : savedCityList) {
            dataList.add(savedCity.getCountyName());
        }
        adapter = new ArrayAdapter<>(ControlActivity.this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String weatherId = savedCityList.get(position).getWeatherId();
                //Log.d(TAG, ""+weatherId);
                //Log.d(TAG, ""+savedCityList.get(position).getCountyName());
                //Log.d(TAG, ""+savedCityList.get(position).getId());
                //Log.d(TAG, ""+savedCityList.get(position).getCityId());
                Intent intent = new Intent(ControlActivity.this, WeatherActivity.class);
                intent.putExtra("weatherId", weatherId);
                startActivity(intent);
                finish();
            }
        });
    }
}

package com.coolweather.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.android.db.SavedCity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ControlActivity extends BaseActivity {
    private List<String> dataList;
    private List<SavedCity> savedCityList;
    private ArrayAdapter<String> adapter;
    private Toolbar mToolbar;
    private static final String TAG = "ControlActivity";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
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
                Log.d(TAG, "weatherId "+weatherId);
                Intent intent = new Intent(ControlActivity.this, WeatherActivity.class);
                intent.putExtra("weatherId", weatherId);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ControlActivity.this);
                dialog.setTitle("确定要删除吗?");
                dialog.setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(SavedCity.class,"id==?",""+
                                savedCityList.get(position).getId());
                        dataList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MyApplication.getContext(),"删除成功^_^",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                return true;
            }
        });


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
}

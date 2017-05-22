package com.coolweather.android;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private SwitchButton mSwitchButton;
    private TextView mUpdateSpaceShow;
    private LinearLayout mUpdateSpace;
    private SwitchButton mSwitchButton1;

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
        mSwitchButton1=(SwitchButton)findViewById(R.id.switch_button1);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwitchButton = (SwitchButton) findViewById(R.id.switch_button);
        mSwitchButton.setOnClickListener(this);
        mUpdateSpaceShow = (TextView) findViewById(R.id.update_space_show);
        mUpdateSpace = (LinearLayout) findViewById(R.id.update_space);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUpdateSpaceShow.setText(preferences.getInt("UpdateTime",6)+"小时");
        mSwitchButton.setChecked(preferences.getBoolean("isChecked",true));
        if (preferences.getBoolean("isChecked",true)){
            mUpdateSpace.setVisibility(View.VISIBLE);
        }else {
            mUpdateSpace.setVisibility(View.GONE);
        }
        mSwitchButton1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(MyApplication.getContext()).edit();
                    editor.putBoolean("isNotification",isChecked);
             
            }
        });
        mSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor  = PreferenceManager.
                            getDefaultSharedPreferences(SettingsActivity.this).edit();
                    editor.putBoolean("isChecked",true);
                    editor.apply();
                    mUpdateSpace.setVisibility(View.VISIBLE);
                }else{
                    SharedPreferences.Editor editor  = PreferenceManager.
                            getDefaultSharedPreferences(SettingsActivity.this).edit();
                    editor.putBoolean("isChecked",false);
                    editor.apply();
                    mUpdateSpace.setVisibility(View.GONE);
                }
            }
        });
        mUpdateSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setItems(getResources().getStringArray(R.array.TimeArray), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        if (arg1 == 0)
                        {
                            mUpdateSpaceShow.setText("1小时");
                        }else if (arg1==1) {
                            mUpdateSpaceShow.setText("2小时");
                        }else if (arg1==2){
                            mUpdateSpaceShow.setText("3小时");
                        }else if (arg1==3){
                            mUpdateSpaceShow.setText("4小时");
                        }else if (arg1==4){
                            mUpdateSpaceShow.setText("5小时");
                        }else if (arg1==5) {
                            mUpdateSpaceShow.setText("6小时");
                        }
                        SharedPreferences.Editor editor  = PreferenceManager.
                                getDefaultSharedPreferences(SettingsActivity.this).edit();
                        editor.putInt("UpdateTime",arg1+1);
                        editor.apply();
                        arg0.dismiss();
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_space:

                break;
        }
    }
}

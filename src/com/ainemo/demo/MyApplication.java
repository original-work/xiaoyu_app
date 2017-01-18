package com.ainemo.demo;

import android.app.Application;

import com.ainemo.sdk.NemoSDK;
import com.ainemo.sdk.model.Settings;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Settings settings = new Settings("0b5d7ed54bee16756a7579c6718ab01e3d1b75eb", true, false);  // develop 鐜
        Settings settings = new Settings("646d65e39c55460da902ff92b7ca2d17e2f57b03", false, false);  // production 鐜

        NemoSDK.getInstance().init(getApplicationContext(), settings);
    }
}

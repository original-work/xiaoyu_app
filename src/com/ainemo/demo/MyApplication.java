package com.ainemo.demo;

import android.app.Application;

import com.ainemo.sdk.NemoSDK;
import com.ainemo.sdk.model.Settings;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Settings settings = new Settings("0b5d7ed54bee16756a7579c6718ab01e3d1b75eb", true, false);  // develop 鐜
//        Settings settings = new Settings("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3", false, false);  // production 鐜

        NemoSDK.getInstance().init(getApplicationContext(), settings);
    }
}

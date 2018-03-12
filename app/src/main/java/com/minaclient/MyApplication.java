package com.minaclient;

import android.app.Application;
import android.content.Context;


/**
 * Created by LIANGSE on 2018/3/12.
 */

public class MyApplication extends Application {


    private static Context context = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}

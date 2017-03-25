package com.lihai.common.app;

import android.app.Application;
import android.content.res.Resources;

/**
 * Created by LiHai on 2017/3/9.
 */
public class CommonApplication extends Application {

    public static Resources resources;


    @Override
    public void onCreate() {
        super.onCreate();
       this. resources = getResources();
    }
}

package com.developer.snapzo;

import android.app.Application;

import com.karumi.dexter.Dexter;

/**
 * Created by subhashsanghani on 9/12/16.
 */
public class MyApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);
    }
}
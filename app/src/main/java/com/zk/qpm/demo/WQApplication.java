package com.zk.qpm.demo;

import android.app.Application;

import com.didichuxing.doraemonkit.DoKit;
import com.didichuxing.doraemonkit.kit.AbstractKit;

import java.util.ArrayList;
import java.util.List;

public class WQApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        List<AbstractKit> kitList = new ArrayList<>();
        new DoKit.Builder(this).customKits(kitList).build();
    }
}

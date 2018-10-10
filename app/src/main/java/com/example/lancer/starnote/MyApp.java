package com.example.lancer.starnote;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * author: Lancer
 * date：2018/9/3
 * des: app类，初始化Bmob
 * email:tyk790406977@126.com
 */

public class MyApp extends Application {
    private String ApplicationId = "1fa189f225d702eb16cc5765d93f4d89";
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, ApplicationId);
    }


}

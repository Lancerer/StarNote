package com.example.lancer.starnote.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


/**
 * author: Lancer
 * date：2018/9/3
 * des:Activity的基类
 * email:tyk790406977@126.com
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initView();
        initData();
    }


    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();

}

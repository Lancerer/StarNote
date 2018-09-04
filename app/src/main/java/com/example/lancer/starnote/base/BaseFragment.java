package com.example.lancer.starnote.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.bmob.v3.b.V;

/**
 * author: Lancer
 * date：2018/9/3
 * des:Fragment的基类
 * email:tyk790406977@126.com
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(initLayout(), container, false);
        initView(rootview);
        initData();
        return rootview;
    }
    protected abstract int initLayout();

    protected abstract void initView(View view);

    protected abstract void initData();

}

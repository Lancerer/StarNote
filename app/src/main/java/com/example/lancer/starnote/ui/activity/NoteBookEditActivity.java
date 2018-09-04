package com.example.lancer.starnote.ui.activity;

import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.base.BaseActivity;
import com.example.lancer.starnote.ui.fragment.NoteBookEditFragment;

public class NoteBookEditActivity extends BaseActivity {

    private android.widget.FrameLayout flPool;

    @Override
    protected int initLayout() {
        return R.layout.activity_note_book_edit;
    }

    @Override
    protected void initView() {

        flPool = findViewById(R.id.fl_pool_editf);
    }

    @Override
    protected void initData() {
        setTitle("编写便签");
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        NoteBookEditFragment fragment = new NoteBookEditFragment();
        ft.replace(R.id.fl_pool_editf, fragment).commit();
    }
}

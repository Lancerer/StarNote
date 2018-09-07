package com.example.lancer.starnote.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.base.BaseActivity;
import com.example.lancer.starnote.ui.fragment.ChangeBgFragment;
import com.example.lancer.starnote.ui.fragment.NoteBookEditFragment;
import com.example.lancer.starnote.ui.fragment.NoteBookFragment;
import com.example.lancer.starnote.util.SystemUtils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    public DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private android.widget.FrameLayout flPool;
    public FloatingActionButton fab;
    private NavigationView navView;
    private NoteBookFragment mNoteBookFragment;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mFab = findViewById(R.id.fab);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        flPool = findViewById(R.id.fl_pool);
        fab = findViewById(R.id.fab);
        navView = findViewById(R.id.nav_view);
    }

    @Override
    protected void initData() {
        setSupportActionBar(mToolbar);
        //todo 点击跳转到便签编辑界面
        setTitle("主页");
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteBookEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(NoteBookEditFragment.ACTION_ADD_SHORTCUT, NoteBookEditFragment.FROM_FAB);
                intent.putExtra("bundle_key", bundle);
                //todo 未完成
                startActivity(intent);

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        initMainFragment();
        initBigPic();//todo 设置背景图
    }

    private void initBigPic() {
        SystemUtils systemUtils = new SystemUtils(this);
        String path = systemUtils.getPath();
        if (path != null) {
            Bitmap bitmap = systemUtils.getBitmapByPath(MainActivity.this, path);
            if (bitmap != null) {
                mDrawer.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            initMainFragment();
        } else if (id == R.id.nav_bg) {
            ChangeBgFragment changeBgFragment = new ChangeBgFragment();
            changeFragment(changeBgFragment);
            fab.hide();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
            share_intent.setType("text/plain");//设置分享内容的类型
            share_intent.putExtra(Intent.EXTRA_SUBJECT, "aaa");//添加分享内容标题
            share_intent.putExtra(Intent.EXTRA_TEXT, "bbb");//添加分享内容
            //创建分享的Dialog
            share_intent = Intent.createChooser(share_intent, "ccc");
            this.startActivity(share_intent);


        } else if (id == R.id.nav_send) {

        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initMainFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mNoteBookFragment = new NoteBookFragment();
        fragmentTransaction.replace(R.id.fl_pool, mNoteBookFragment).commit();
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool, fragment).commit();
    }
}

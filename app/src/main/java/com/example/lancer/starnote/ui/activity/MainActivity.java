package com.example.lancer.starnote.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lancer.starnote.BuildConfig;
import com.example.lancer.starnote.R;
import com.example.lancer.starnote.base.BaseActivity;
import com.example.lancer.starnote.ui.fragment.ChangeBgFragment;
import com.example.lancer.starnote.ui.fragment.NoteBookEditFragment;
import com.example.lancer.starnote.ui.fragment.NoteBookFragment;
import com.example.lancer.starnote.util.Sputil;
import com.example.lancer.starnote.util.SystemUtils;
import com.example.lancer.starnote.widget.CircleImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;


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
    private CircleImageView mHeadImg = null;
    private android.widget.TextView tvSelectGallery;
    private android.widget.TextView tvSelectCamera;


    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @SuppressLint("CutPasteId")
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
        View headerView = navView.getHeaderView(0);
        mHeadImg = headerView.findViewById(R.id.imageView);


    }

    @Override
    protected void onResume() {
        super.onResume();
     /*   if(mHeadImg==null){
            String headImg = (String) Sputil.get(this, "headImg", "");
            byte[] decode = Base64.decode(headImg, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            mHeadImg.setImageBitmap(bitmap);
        }*/

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
                startActivity(intent);

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        initMainFragment();
        initBigPic();//todo 更换背景图

        mHeadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeadDialog();
            }
        });
    }

    /**
     * 更换头像的Dialog
     */
    private void showHeadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_headimg, null);
        tvSelectGallery = view.findViewById(R.id.tv_select_gallery);
        tvSelectCamera = view.findViewById(R.id.tv_select_camera);
        tvSelectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 调用相机
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // 判断存储卡是否可用，存储照片文件
                if (hasSdcard()) {
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                }

                startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
            }
        });
        tvSelectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 调用相册
                Intent intentFromGallery = new Intent();
                // 设置文件类型
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);//设置头像
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
          /*  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String Stringimg = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
            Sputil.put(this, "headImg", Stringimg);*/
            mHeadImg.setImageBitmap(photo);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
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

    private long mBackPressedTime = 0;

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            long curTime = SystemClock.uptimeMillis();
            if ((curTime - mBackPressedTime) < (2 * 1000)) {
                finish();
                System.exit(0);
            } else {
                mBackPressedTime = curTime;
                Snackbar.make(mDrawer, "在按一次退出程序", Snackbar.LENGTH_LONG).show();
            }
            //   super.onBackPressed();
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
            share_intent.putExtra(Intent.EXTRA_TEXT, "欢迎使用繁星笔记");//添加分享内容
            //创建分享的Dialog
            share_intent = Intent.createChooser(share_intent, "app推荐");
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

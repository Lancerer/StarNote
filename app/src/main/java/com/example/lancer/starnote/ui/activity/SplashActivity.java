package com.example.lancer.starnote.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lancer.starnote.R;
import com.example.lancer.starnote.base.BaseActivity;
import com.example.lancer.starnote.bean.User;
import com.example.lancer.starnote.util.Sputil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SplashActivity extends BaseActivity {

    private android.widget.ImageView ivSpalsh;
    private boolean mIsFirst;

    @Override
    protected int initLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

        ivSpalsh = findViewById(R.id.iv_spalsh);
    }

    @Override
    protected void initData() {
        Glide.with(this).load(R.drawable.wel).into(ivSpalsh);
        mIsFirst = (boolean) Sputil.get(this, "isFirst", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              goToMainActivity();
            }
        }, 2000);
    }

    private void autoLogin() {
        if (Sputil.contains(this, "user_name")) {
            String name = (String) Sputil.get(this, "email", "");
            String password = (String) Sputil.get(this, "password", "");
        /*    BmobUser user = new BmobUser();
            user.setUsername(name);
            user.setPassword(password);*/

            User user1 = new User();
            user1.setName(name);
            user1.setPassword(password);
            user1.login(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        Toast.makeText(SplashActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        Toast.makeText(SplashActivity.this, "保存到Bmob失败", Toast.LENGTH_SHORT).show();
                        goToLoginActivity();
                    }
                }
            });
         /*   user.login(new SaveListener<User>() {
                @Override
                public void done(User s, BmobException e) {
                    if (e == null) {
                        Toast.makeText(SplashActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    } else {
                        Toast.makeText(SplashActivity.this, "保存到Bmob失败", Toast.LENGTH_SHORT).show();
                        goToLoginActivity();
                    }
                }
            });*/


        } else {
            goToLoginActivity();
        }
    }

    private void goToMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        this.finish();
    }

    private void goToLoginActivity() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        this.finish();
    }
}

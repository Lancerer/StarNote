package com.example.lancer.starnote.ui.activity;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.base.BaseActivity;
import com.example.lancer.starnote.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity {


    private android.widget.ProgressBar loginProgress;
    private android.widget.ScrollView loginForm;
    private android.widget.LinearLayout emailLoginForm;
    private EditText email;
    private EditText password;
    private Button emailSignInButton;
    private android.widget.TextView tvRegister;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        loginProgress = findViewById(R.id.login_progress);
        loginForm = findViewById(R.id.login_form);
        emailLoginForm = findViewById(R.id.email_login_form);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        emailSignInButton = findViewById(R.id.email_sign_in_button);
        tvRegister = findViewById(R.id.tv_register);
    }

    @Override
    protected void initData() {
        emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = LoginActivity.this.email.getText().toString();
                String password = LoginActivity.this.password.getText().toString();
        /*        BmobUser user = new BmobUser();
                user.setUsername(email);
                user.setPassword(password);*/
                User user1 = new User();
                user1.setName(email);
                user1.setPassword(password);
                user1.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            goToMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

              /*  user.login(new SaveListener<User>() {
                    @Override
                    public void done(User o, BmobException e) {
                        if (e == null) {
                            goToMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
            }
        });
        tvRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });
    }

    private void goToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        this.finish();
    }

    private void goToRegisterActivity() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        this.finish();
    }
}


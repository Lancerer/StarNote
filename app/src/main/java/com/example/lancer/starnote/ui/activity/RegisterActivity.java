package com.example.lancer.starnote.ui.activity;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.base.BaseActivity;
import com.example.lancer.starnote.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends BaseActivity {


    private android.widget.ProgressBar loginProgress;
    private android.widget.ScrollView loginForm;
    private android.widget.LinearLayout emailLoginForm;
    private android.widget.EditText etName;
    private android.widget.EditText etPassword;
    private android.widget.EditText etPasswordAgain;
    private android.widget.Button emailSignInButton;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

        loginProgress = findViewById(R.id.login_progress);
        loginForm = findViewById(R.id.login_form);
        emailLoginForm = findViewById(R.id.email_login_form);
        etName = findViewById(R.id.et_name);
        etPassword = findViewById(R.id.et_password);
        etPasswordAgain = findViewById(R.id.et_password_again);
        emailSignInButton = findViewById(R.id.email_sign_in_button);
    }

    @Override
    protected void initData() {
        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String etpassword = etPassword.getText().toString();
                String etpasswordagain = etPasswordAgain.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(etpassword) || TextUtils.isEmpty(etpasswordagain)) {
                    Toast.makeText(RegisterActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                }
                if (!etpassword.equals(etpasswordagain)) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                }
               BmobUser user = new BmobUser();
                user.setUsername(name);
                user.setPassword(etpassword);
                User user1 = new User();
                user1.setName(name);
                user1.setPassword(etpassword);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User o, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

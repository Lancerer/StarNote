package com.example.lancer.starnote.bean;


import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * author: Lancer
 * date：2018/9/3
 * des:
 * email:tyk790406977@126.com
 */

public class User extends BmobUser implements Serializable {

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

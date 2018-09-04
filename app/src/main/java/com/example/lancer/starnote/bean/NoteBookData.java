package com.example.lancer.starnote.bean;

import android.support.annotation.NonNull;

import cn.bmob.v3.BmobObject;

/**
 * author: Lancer
 * date：2018/9/4
 * des:
 * email:tyk790406977@126.com
 */

public class NoteBookData extends BmobObject implements Comparable<NoteBookData> {


    private int id;
    private int iid;
    private String userId;
    private String unixTime;
    private String date;
    private String content;
    private String colorText;
    private int color;
    public static final String NOTE_USER_ID = "userId";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public static String getNoteUserId() {
        return NOTE_USER_ID;
    }

    @Override
    public int compareTo(@NonNull NoteBookData o) {
        return 0;
    }
    public int getColor() {
        // 客户端始终以当前手机上的颜色为准
        if ("blue".equals(colorText)) {
            this.color = 3;
        } else if ("red".equals(colorText)) {
            this.color = 2;
        } else if ("yellow".equals(colorText)) {
            this.color = 1;
        } else if ("purple".equals(colorText)) {
            this.color = 4;
        } else if ("green".equals(colorText)) {
            this.color = 0;
        }
        return color;
    }
    public void setColor(int color) {
        switch (color) {
            case 0:
                colorText = "green";
                break;
            case 1:
                colorText = "yellow";
                break;
            case 2:
                colorText = "red";
                break;
            case 3:
                colorText = "blue";
                break;
            case 4:
                colorText = "purple";
                break;
            default:
                this.color = color;
                break;
        }
    }
}

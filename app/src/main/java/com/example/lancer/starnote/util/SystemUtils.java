package com.example.lancer.starnote.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;

/**
 * author: Lancer
 * date：2018/9/3
 * des:
 * email:tyk790406977@126.com
 */

public class SystemUtils {

    private Context mContext;
    //SharedPreferences文件名
    private String PREF_NAME = "config";
    private final String KEY_DRAFT = "key_draft";
    private final String BG_PIC_PATH = "bg_pic_path";

    public SystemUtils(Context context) {
        mContext = context;
    }

    /**
     * 获得便签的草稿
     */
    public String getNoteDraft() {
        return getPreferences().getString(KEY_DRAFT, "");
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 设置草稿
     *
     * @param str
     */
    public void setNoteDraft(String str) {
        set(KEY_DRAFT, str);
    }

    public String getPath() {
        return getString(BG_PIC_PATH);
    }

    public String getString(String str) {
        SharedPreferences share = getPreferences();
        return share.getString(str, null);
    }

    public SharedPreferences getPreferences() {
        SharedPreferences pre = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    public void saveBgPicPath(String path) {
        set(BG_PIC_PATH, path);
        //Sputil.put(mContext, BG_PIC_PATH, path);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public Bitmap getBitmapByPath(Activity aty, String path) {
        AssetManager am = aty.getAssets();
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = am.open(path);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void ShowNote(String str, Activity activity) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, str);
        intent = Intent.createChooser(intent, "繁星笔记便签分享");
        activity.startActivity(intent);
    }


}

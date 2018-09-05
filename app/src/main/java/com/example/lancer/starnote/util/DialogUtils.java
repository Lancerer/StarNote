package com.example.lancer.starnote.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * author: Lancer
 * date：2018/9/5
 * des:
 * email:tyk790406977@126.com
 */

public class DialogUtils {
    /**
     * 获得一个Dialog
     *
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context, String msg, DialogInterface.OnClickListener OkClick, DialogInterface.OnClickListener ExitClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", OkClick);
        builder.setNegativeButton("取消", ExitClick);
        return builder;
    }


}

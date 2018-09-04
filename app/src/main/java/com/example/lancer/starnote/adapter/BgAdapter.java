package com.example.lancer.starnote.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.bean.BgPic;
import com.example.lancer.starnote.util.SystemUtils;

import java.util.List;

/**
 * author: Lancer
 * date：2018/9/3
 * des:
 * email:tyk790406977@126.com
 */

public class BgAdapter extends BaseAdapter {
    private List<BgPic> mList;
    private Resources mResources;
    private Context mContext;
    private SystemUtils mSystemUtils;
    private String mDefaultBgPath;

    public BgAdapter(List<BgPic> list, Context context) {
        mList = list;
        mResources = context.getResources();
        mContext = context;
        mSystemUtils = new SystemUtils(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BgPic getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bg_pic_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivcheck = convertView.findViewById(R.id.iv_check);
            viewHolder.ivitembg = convertView.findViewById(R.id.iv_item_bg);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BgPic item = getItem(position);
        viewHolder.ivitembg.setBackgroundDrawable(new BitmapDrawable(mResources, item.mBitmap));
        mDefaultBgPath = mSystemUtils.getPath();
        if (item.path.equals(mDefaultBgPath)) {
            viewHolder.ivcheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivcheck.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView ivcheck, ivitembg;
    }

}

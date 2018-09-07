package com.example.lancer.starnote.ui.fragment;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.adapter.BgAdapter;
import com.example.lancer.starnote.base.BaseFragment;
import com.example.lancer.starnote.bean.BgPic;
import com.example.lancer.starnote.ui.activity.MainActivity;
import com.example.lancer.starnote.util.SystemUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ChangeBgFragment extends BaseFragment {
    private List<BgPic> mLists;
    private BgAdapter mAdapter;
    private android.widget.GridView gvChange;
    private SystemUtils systemUtils;

    @Override
    protected int initLayout() {
        return R.layout.fragment_change_bg;
    }

    @Override
    protected void initView(View view) {

        gvChange = view.findViewById(R.id.gv_change);
    }

    @Override
    protected void initData() {
        initBackgroundPic();
        mAdapter = new BgAdapter(mLists, getContext());
        gvChange.setAdapter(mAdapter);
        gvChange.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mLists.get(position).path;

                systemUtils = new SystemUtils(getActivity());
                systemUtils.saveBgPicPath(path);
                Bitmap bitmap = systemUtils.getBitmapByPath(getActivity(), path);
                if (bitmap != null) {
                    //todo 设置Drawer的背景图片，就是更换背景。
                    ((MainActivity) getActivity()).mDrawer.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                    mAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    /**
     * 将assets中的图片资源及路径存储到集合中
     */
    private void initBackgroundPic() {
        AssetManager am = getActivity().getAssets();
        try {
            String[] drawableList = am.list("");
            mLists = new ArrayList<>();
            for (String path : drawableList) {
                BgPic bg = new BgPic();
                Log.d("changeFragmentDEBUG", path);
                InputStream is = am.open(path);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bg.path = path;
                bg.mBitmap = bitmap;
                mLists.add(bg);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package com.example.lancer.starnote.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.adapter.DragAdapter;
import com.example.lancer.starnote.base.BaseFragment;
import com.example.lancer.starnote.bean.NoteBookData;
import com.example.lancer.starnote.db.NoteDataDao;
import com.example.lancer.starnote.ui.activity.MainActivity;
import com.example.lancer.starnote.ui.activity.NoteBookEditActivity;
import com.example.lancer.starnote.util.AnimUtils;
import com.example.lancer.starnote.widget.DragGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBookFragment extends BaseFragment {
    private com.example.lancer.starnote.widget.DragGridView dragView;
    private NoteDataDao mNoteDataDao;
    private List<NoteBookData> mLists;
    private DragAdapter mDragAdapter;
    private Activity mActivity;
    private android.widget.ImageView ivTrash;


    @Override
    protected int initLayout() {
        return R.layout.fragment_note_book;
    }

    @Override
    protected void initView(View view) {

        dragView = view.findViewById(R.id.dragView);
        ivTrash = view.findViewById(R.id.iv_trash);
    }

    @Override
    protected void initData() {
        mActivity = getActivity();
        mNoteDataDao = new NoteDataDao(mActivity);
        mLists = mNoteDataDao.query();
        if (mLists != null) {
            mDragAdapter = new DragAdapter(mActivity, mLists);
        }
        dragView.setAdapter(mDragAdapter);
        dragView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt(NoteBookEditFragment.WHERE_FROM, NoteBookEditFragment.FROM_ITEM);
                bundle.putSerializable("list", mLists.get(position));
                Intent intent = new Intent(getActivity(), NoteBookEditActivity.class);
                intent.putExtra("bundle_key", bundle);
                startActivity(intent);
            }
        });
        //设置GirdView被选中后的背景颜色
        dragView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        dragView.setTrashView(ivTrash);
        dragView.setOnDeleteListener(new DragGridView.onDeleteListener() {
            @Override
            public void onDelete(int position) {
                delete(position);
            }
        });
        dragView.setOnMoveListener(new DragGridView.onMoveListener() {
            @Override
            public void startMove() {
                ivTrash.startAnimation(AnimUtils.getTranslateAnimation(0, 0, ivTrash.getTop(), 0, 500));
                ivTrash.setVisibility(View.VISIBLE);
            }

            @Override
            public void finishMove() {

                ivTrash.startAnimation(AnimUtils.getTranslateAnimation(0, 0, 0, ivTrash.getTop(), 500));
                ivTrash.setVisibility(View.INVISIBLE);
                if (mDragAdapter.getDataChange()) {
                    new Thread() {
                        @Override
                        public void run() {
                            mNoteDataDao.reset(mLists);
                        }
                    }.start();
                }
            }

            @Override
            public void cancleMove() {

            }
        });
    }

    /**
     * 删除便签方法
     *
     * @param position
     */
    private void delete(int position) {
        int id = mLists.get(position).getId();
        mNoteDataDao.delete(id);
        mLists.remove(position);
        if (mLists != null && mDragAdapter != null) {
            mDragAdapter.refurbishData(mLists);
            dragView.setAdapter(mDragAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refurbish();
        ((MainActivity) getActivity()).fab.show();
    }

    /**
     * 查询数据库刷新界面
     */
    private void refurbish() {
        mLists = mNoteDataDao.query();
        if (mLists != null) {
            if (mDragAdapter != null) {
                mDragAdapter.refurbishData(mLists);
            } else {
                mDragAdapter = new DragAdapter(mActivity, mLists);
                dragView.setAdapter(mDragAdapter);
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mNoteDataDao.destroy();
    }
}

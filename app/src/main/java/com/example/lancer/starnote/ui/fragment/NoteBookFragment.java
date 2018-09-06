package com.example.lancer.starnote.ui.fragment;


import android.app.Activity;
import android.view.View;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.adapter.DragAdapter;
import com.example.lancer.starnote.base.BaseFragment;
import com.example.lancer.starnote.bean.NoteBookData;
import com.example.lancer.starnote.db.NoteDataDao;
import com.example.lancer.starnote.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBookFragment extends BaseFragment {

    private com.example.lancer.starnote.widget.DragGridView dragView;
    private NoteDataDao mNoteDataDao;
    private List<NoteBookData> mLists;
    private DragAdapter mDragAdapter;
    private Activity mActivity;


    @Override
    protected int initLayout() {
        return R.layout.fragment_note_book;
    }

    @Override
    protected void initView(View view) {

        dragView = view.findViewById(R.id.dragView);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        refurbish();
        ((MainActivity) getActivity()).fab.show();
    }
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

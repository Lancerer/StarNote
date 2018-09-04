package com.example.lancer.starnote.ui.fragment;


import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.base.BaseFragment;
import com.example.lancer.starnote.bean.NoteBookData;
import com.example.lancer.starnote.util.AnimUtils;

public class NoteBookEditFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {


    private android.widget.EditText noteDetailEdit;
    private android.widget.ImageView noteDetailImgMenu;
    private com.example.lancer.starnote.widget.NoteItemCircleView noteDetailImgGreen;
    private com.example.lancer.starnote.widget.NoteItemCircleView noteDetailImgBlue;
    private com.example.lancer.starnote.widget.NoteItemCircleView noteDetailImgPurple;
    private com.example.lancer.starnote.widget.NoteItemCircleView noteDetailImgYellow;
    private com.example.lancer.starnote.widget.NoteItemCircleView noteDetailImgRed;
    private android.widget.RelativeLayout noteDetailTitlebar;
    private android.widget.TextView noteDetailTvDate;
    private android.widget.ImageView noteDetailImgButton;
    private android.widget.ImageView noteDetailImgThumbtack;
    private com.github.clans.fab.FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton menuItemTextFont;
    private com.github.clans.fab.FloatingActionButton menuItemClock;
    private com.github.clans.fab.FloatingActionButton menuItemDesktop;
    private com.github.clans.fab.FloatingActionButton menuItemShare;
    private android.widget.LinearLayout fontSizeSelector;
    private android.widget.FrameLayout llFontSmall;
    private android.widget.ImageView ivSmallSelect;
    private android.widget.FrameLayout llFontNormal;
    private android.widget.ImageView ivMediumSelect;
    private android.widget.FrameLayout llFontLarge;
    private android.widget.ImageView ivLargeSelect;
    private android.widget.FrameLayout llFontSuper;
    private android.widget.ImageView ivSuperSelect;
    private RelativeLayout mLayoutMenu;
    private NoteBookData mNoteBookData;

    @Override
    protected int initLayout() {
        return R.layout.fragment_note_book_edit;

    }

    @Override
    protected void initView(View view) {
        mLayoutMenu = view.findViewById(R.id.note_detail_menu);
        noteDetailEdit = view.findViewById(R.id.note_detail_edit);
        noteDetailImgMenu = view.findViewById(R.id.note_detail_img_menu);
        noteDetailImgGreen = view.findViewById(R.id.note_detail_img_green);
        noteDetailImgBlue = view.findViewById(R.id.note_detail_img_blue);
        noteDetailImgPurple = view.findViewById(R.id.note_detail_img_purple);
        noteDetailImgYellow = view.findViewById(R.id.note_detail_img_yellow);
        noteDetailImgRed = view.findViewById(R.id.note_detail_img_red);
        noteDetailTitlebar = view.findViewById(R.id.note_detail_titlebar);
        noteDetailTvDate = view.findViewById(R.id.note_detail_tv_date);
        noteDetailImgButton = view.findViewById(R.id.note_detail_img_button);
        noteDetailImgThumbtack = view.findViewById(R.id.note_detail_img_thumbtack);
        floatingActionMenu = view.findViewById(R.id.floating_action_menu);
        menuItemTextFont = view.findViewById(R.id.menu_item_text_font);
        menuItemClock = view.findViewById(R.id.menu_item_clock);
        menuItemDesktop = view.findViewById(R.id.menu_item_desktop);
        menuItemShare = view.findViewById(R.id.menu_item_share);
        fontSizeSelector = view.findViewById(R.id.font_size_selector);
        llFontSmall = view.findViewById(R.id.ll_font_small);
        ivSmallSelect = view.findViewById(R.id.iv_small_select);
        llFontNormal = view.findViewById(R.id.ll_font_normal);
        ivMediumSelect = view.findViewById(R.id.iv_medium_select);
        llFontLarge = view.findViewById(R.id.ll_font_large);
        ivLargeSelect = view.findViewById(R.id.iv_large_select);
        llFontSuper = view.findViewById(R.id.ll_font_super);
        ivSuperSelect = view.findViewById(R.id.iv_super_select);
    }


    @Override
    protected void initData() {
        initListener();
    }

    private void initListener() {
        noteDetailImgBlue.setOnClickListener(this);
        noteDetailImgGreen.setOnClickListener(this);
        noteDetailImgPurple.setOnClickListener(this);
        noteDetailImgRed.setOnClickListener(this);
        noteDetailImgYellow.setOnClickListener(this);

        noteDetailImgButton.setOnTouchListener(this);//todo 点击调色板调整背景颜色
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.note_detail_img_green:
                mNoteBookData.setColor(0);
                break;
            case R.id.note_detail_img_blue:
                mNoteBookData.setColor(3);
                break;
            case R.id.note_detail_img_purple:
                mNoteBookData.setColor(4);
                break;
            case R.id.note_detail_img_yellow:
                mNoteBookData.setColor(1);
                break;
            case R.id.note_detail_img_red:
                mNoteBookData.setColor(2);
                break;

        }
    }

    /**
     * 点击调色板调整背景颜色
     * 当他是gone时弹出，反之收回
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (mLayoutMenu.getVisibility() == View.GONE) {
                openMenu();
            } else {
                closeMenu();
            }
        }
        return true;
    }

    private void closeMenu() {
        AnimUtils.CloseAnimation(mLayoutMenu, noteDetailImgButton, 800);
    }

    private void openMenu() {
        AnimUtils.OpenAnimation(mLayoutMenu, noteDetailImgButton, 500);
    }
}

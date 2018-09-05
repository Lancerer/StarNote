package com.example.lancer.starnote.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lancer.starnote.R;
import com.example.lancer.starnote.base.BaseFragment;
import com.example.lancer.starnote.bean.NoteBookData;
import com.example.lancer.starnote.db.NoteDataDao;
import com.example.lancer.starnote.ui.activity.MainActivity;
import com.example.lancer.starnote.util.AnimUtils;
import com.example.lancer.starnote.util.Constants;
import com.example.lancer.starnote.util.DialogUtils;
import com.example.lancer.starnote.util.StringUtils;
import com.example.lancer.starnote.util.SystemUtils;

import java.util.HashMap;
import java.util.Map;

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
    private NoteDataDao mNoteDataDao;
    private boolean isNewNote;
    //图钉
    public static final int[] sThumbtackImgs = {R.drawable.green,
            R.drawable.yellow, R.drawable.red, R.drawable.blue,
            R.drawable.purple};
    //editText的背景色
    public static final int[] sBackGrounds = {0xffe5fce8,// 绿色
            0xfffffdd7,// 黄色
            0xffffddde,// 红色
            0xffccf2fd,// 蓝色
            0xfff7f5f6,// 紫色
    };
    private com.github.clans.fab.FloatingActionButton menuItemSave;

    private int SizeId;
    private static final Map<Integer, Integer> TextFontSelectMap = new HashMap<>();

    static {
        TextFontSelectMap.put(Constants.SMALL, R.id.iv_small_select);
    }

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
        menuItemSave = view.findViewById(R.id.menu_item_save);
    }


    @Override
    protected void initData() {
        mNoteDataDao = new NoteDataDao(getContext());
        if (mNoteBookData == null) {
            mNoteBookData = new NoteBookData();
            mNoteBookData.setContent("欢迎使用繁星笔记");
            isNewNote = true;

        }
        if (StringUtils.isEmpty(mNoteBookData.getDate())) {
            mNoteBookData.setDate(StringUtils.getDataTime("yyyy/MM/dd"));
        }
        initListener();
        //设置图钉图片
        noteDetailImgThumbtack.setImageResource(sThumbtackImgs[mNoteBookData.getColor()]);
        //设置日期
        noteDetailTvDate.setText(mNoteBookData.getDate());
        //设置EditText相关事宜
        noteDetailEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        noteDetailEdit.setHorizontallyScrolling(false);
        noteDetailEdit.setSingleLine(false);
        noteDetailEdit.setText(Html.fromHtml(mNoteBookData.getContent().toString()));
        noteDetailEdit.setBackgroundColor(sBackGrounds[mNoteBookData.getColor()]);


    }

    private void initListener() {
        noteDetailImgBlue.setOnClickListener(this);
        noteDetailImgGreen.setOnClickListener(this);
        noteDetailImgPurple.setOnClickListener(this);
        noteDetailImgRed.setOnClickListener(this);
        noteDetailImgYellow.setOnClickListener(this);

        menuItemClock.setOnClickListener(this);
        menuItemDesktop.setOnClickListener(this);
        menuItemSave.setOnClickListener(this);
        menuItemShare.setOnClickListener(this);
        menuItemTextFont.setOnClickListener(this);

        noteDetailImgButton.setOnTouchListener(this);//todo 点击调色板调整背景颜色
        mLayoutMenu.setOnTouchListener(this);


    }

 /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    *//**
     * 保存按钮
     *
     * @param menu
     * @param inflater
     *//*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notebook_edit_save, menu);
    }

    *//**
     * 保存按钮点击保存
     *
     * @param item
     * @return
     *//*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                if (!StringUtils.isEmpty(noteDetailEdit.getText().toString())) {
                    save();
                    getActivity().finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }*/


    /**
     * 保存笔记
     */
    private void save() {
        setNote();
        mNoteDataDao.save(mNoteBookData);
    }

    /**
     * 保存到数据库中
     */
    private void setNote() {
        if (mNoteBookData.getId() == 0) {
            mNoteBookData.setId(-1
                    * StringUtils.toInt(
                    StringUtils.getDataTime("dddHHmmss"), 0));
        }
        // String userId = AccountUtils.getUserId(getActivity());
        //mNoteBookData.setUserId(userId);

        mNoteBookData.setUnixTime(StringUtils.getDataTime("yyyy-MM-dd HH:mm:ss"));
        mNoteBookData.setContent(noteDetailEdit.getText().toString());
        mNoteBookData.setObjectId(mNoteBookData.getObjectId());
    }

    @Override
    public void onClick(View v) {
        String NoteText = noteDetailEdit.getText().toString();
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


            case R.id.menu_item_save:
                if (!StringUtils.isEmpty(noteDetailEdit.getText().toString())) {
                    save();
                    getActivity().finish();
                }
                break;
            case R.id.menu_item_clock:
                if (noteDetailEdit.equals("")) {
                    Toast.makeText(getActivity(), "您还没有编写便签", Toast.LENGTH_SHORT).show();
                } else {
                    setClock(NoteText);
                }
                break;
            case R.id.menu_item_desktop:
                addToDesktop(NoteText);
                break;
            case R.id.menu_item_share:
                if (noteDetailEdit.equals("")) {
                    Toast.makeText(getActivity(), "您还没有编写便签", Toast.LENGTH_SHORT).show();
                } else {
                    new SystemUtils(getContext()).ShowNote(NoteText, getActivity());
                }
                break;
            case R.id.menu_item_text_font:
                setTextFont();
                break;


            case R.id.ll_font_small:
                break;
            case R.id.ll_font_normal:
                break;
            case R.id.ll_font_large:
                break;
            case R.id.ll_font_super:
                break;

        }
    }

    /**
     * 设置字体大小方法
     */
    private void setTextFont() {

    }

    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    /**
     * 添加便签到桌面
     *
     * @param noteText
     */
    private void addToDesktop(String noteText) {
        Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, noteText);

        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getActivity(),
                        R.drawable.blue));

        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(getActivity(), MainActivity.class);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        addShortcutIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        getActivity().sendBroadcast(addShortcutIntent);
        Toast.makeText(getActivity(), "已添加至桌面", Toast.LENGTH_LONG).show();
    }

    /**
     * 设置闹铃
     *
     * @param noteText 便签写的东西
     */
    private void setClock(String noteText) {
        //todo 闹铃功能未实现
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

    public boolean onBackPressed() {
        if (isNewNote) {
            final String NoteText = noteDetailEdit.getText().toString();
            if (!TextUtils.isEmpty(NoteText)) {
                DialogUtils.getDialog(getContext(), "是否保存为草稿",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new SystemUtils(getContext()).setNoteDraft(NoteText + "[草稿]");
                                getActivity().finish();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new SystemUtils(getContext()).getNoteDraft();
                                getActivity().finish();
                            }
                        }).show();
                return true;
            }
        }
        return false;
    }
}

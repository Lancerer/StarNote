package com.example.lancer.starnote.ui.fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lancer.starnote.MyApp;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
    //放时间的bg
    public static final int[] sTitleBackGrounds = {
            0xffcef3d4,// 绿色
            0xffebe5a9,// 黄色
            0xffecc4c3,// 红色
            0xffa9d5e2,// 蓝色
            0xffddd7d9,// 紫色
    };
    //Fab保存功能
    private com.github.clans.fab.FloatingActionButton menuItemSave;

    private AlarmManager mAlarmManager;

    public static final String WHERE_FROM = "DATA_FROM_WHERE";
    public static final int FROM_FAB = 1;
    public static final int FROM_ITEM = 0;
    private int whereFrom = FROM_FAB;// 从哪个界面来

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
      //  menuItemTextFont = view.findViewById(R.id.menu_item_text_font);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle_key = getActivity().getIntent().getBundleExtra("bundle_key");
        if (bundle_key != null) {
            mNoteBookData = (NoteBookData) bundle_key.getSerializable("list");
            whereFrom = bundle_key.getInt(WHERE_FROM, FROM_FAB);
        }
    }

    @Override
    protected void initData() {
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //初始化数据库数据
        mNoteDataDao = new NoteDataDao(getContext());
        if (mNoteBookData == null) {
            mNoteBookData = new NoteBookData();
            mNoteBookData.setContent(new SystemUtils(getActivity()).getNoteDraft());
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
        //调节便利贴背景颜色
        noteDetailImgBlue.setOnClickListener(this);
        noteDetailImgGreen.setOnClickListener(this);
        noteDetailImgPurple.setOnClickListener(this);
        noteDetailImgRed.setOnClickListener(this);
        noteDetailImgYellow.setOnClickListener(this);
        //Fab的点击事件
        menuItemClock.setOnClickListener(this);
        menuItemDesktop.setOnClickListener(this);
        menuItemSave.setOnClickListener(this);
        menuItemShare.setOnClickListener(this);

        //todo 点击调色板调整背景颜色
        noteDetailImgButton.setOnTouchListener(this);
        mLayoutMenu.setOnTouchListener(this);
    }
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
            case R.id.ll_font_small:
                noteDetailEdit.setTextSize(100);
                fontSizeSelector.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_font_normal:
                break;
            case R.id.ll_font_large:
                break;
            case R.id.ll_font_super:
                break;

        }
        //更换便利贴的颜色
        noteDetailImgThumbtack.setImageResource(sThumbtackImgs[mNoteBookData.getColor()]);
        noteDetailEdit.setBackgroundColor(sBackGrounds[mNoteBookData.getColor()]);
        noteDetailTitlebar.setBackgroundColor(sTitleBackGrounds[mNoteBookData.getColor()]);
        closeMenu();
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
        Calendar calendar = Calendar.getInstance();
         int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Intent intent = new Intent(getActivity(), RingReceived.class);
                PendingIntent sender = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));   // 这里时区需要设置一下，不然会有8个小时的时间差
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long selectTime = calendar.getTimeInMillis();
                // 进行闹铃注册，设置每天的提醒时间
                 mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime, 24 * 60 * 60 * 1000, sender);
                Toast.makeText(getContext(), "设置成功! ", Toast.LENGTH_LONG).show();
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    public static class RingReceived extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("test", "闹钟响了");
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
           MediaPlayer mMediaPlayer= new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(context, alert);
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    /**
     * 返回键保存草稿
     *
     * @return
     */
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
                                new SystemUtils(getContext()).setNoteDraft("");
                                getActivity().finish();
                            }
                        }).show();
                return true;
            }
        }
        return false;
    }
}

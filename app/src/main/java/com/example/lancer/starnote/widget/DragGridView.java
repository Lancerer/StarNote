package com.example.lancer.starnote.widget;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.example.lancer.starnote.util.Constants;

/**
 * author: Lancer
 * date：2018/9/6
 * des:可拖拽的GridView
 * 根据手指按下的X,Y坐标来获取我们在GridView上面点击的item
 * 手指按下的时候使用Handler和Runnable来实现一个定时器，假如定时时间为1000毫秒，在1000毫秒内，如果手指抬起了移除定时器，没有抬起并且手指点击在GridView的item所在的区域，则表示我们长按了GridView的item
 * 如果我们长按了item则隐藏item,然后使用WindowManager来添加一个item的镜像在屏幕用来代替刚刚隐藏的item
 * 当我们手指在屏幕移动的时候，更新item镜像的位置，然后在根据我们移动的X,Y的坐标来获取移动到GridView的哪一个位置
 * 到GridView的item过多的时候，可能一屏幕显示不完，我们手指拖动item镜像到屏幕下方，要触发GridView想上滚动，同理，当我们手指拖动item镜像到屏幕上面，触发GridView向下滚动
 * GridView交换数据，刷新界面，移除item的镜像
 * email:tyk790406977@126.com
 */

public class DragGridView extends GridView {
    private static final int MOVE_OFFSET = 25;

    /**
     * item长按响应时间
     */
    private long dragResponseMinute = 1000;
    /**
     * 正被拖拽的Position
     */
    private int mDragPosition;

    private GridBaseAdapter mDragAdapter;
    private boolean isDrag = false;//是否可以拖拽
    private boolean canDrag = true;//是否可用拖拽，主要用于外部开放设置
    private boolean mAnimateEnd = true;

    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;

    private View FirstDragedView = null;//第一个被拖拽的item对应的View
    private ImageView mDragImageView;//拖拽时创建出来的幻影镜像
    private Bitmap mDragBitmap;//幻影镜像对应的Bitmap；
    private View mTrashView;//删除item的垃圾桶图标

    private Vibrator mVibrator;//震动器
    private int mStatusHeight;//状态栏的高度
    private WindowManager mWindowManager;//窗口管理器
    private WindowManager.LayoutParams mWinLayoutParams;//item镜像的布局参数

    private int mPoint2ItemTop; // 按下的点到所在item的上边缘的距离
    private int mPoint2ItemLeft;
    private int mOffset2Top; // DragGridView距离屏幕顶部的偏移量
    private int mOffset2Left;


    private final TouchRect moveRect = new TouchRect();
    private final TouchRect gridRect = new TouchRect();
    private final TouchRect trashRect = new TouchRect();

    private int mDownScrollBorder; // DragGridView自动向下滚动的边界值
    private int mUpScrollBorder; // DragGridView自动向上滚动的边界值

    private static final int speed = 20;//DragView自动滚动的速度
    private boolean moved = false;

    private static onMoveListener mMoveListener;//拖拽开始和结束的监听
    private onDeleteListener mDeleteListener;//移动到垃圾桶的监听
    private OnChanageListener onChanageListener;

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context);

        //todo
    }


    /**
     * 设置响应拖拽的毫秒数，默认是1000毫秒
     *
     * @param dragResponseMS
     */
    public void setDragResponseMS(long dragResponseMS) {
        this.dragResponseMinute = dragResponseMS;
    }

    /**
     * 设置垃圾桶图标
     *
     * @param trashView
     */
    public void setTrashView(View trashView) {
        this.mTrashView = trashView;
    }

    //todo
    public void setDragEnable(boolean isDrag) {
        this.canDrag = isDrag;
        if (canDrag) {
            handler.removeCallbacks(mLongClickRunnable);
        }
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    private int getStatusHeight(Context context) {
        return (int) Math.ceil(20 * context.getResources().getDisplayMetrics().density);
    }

    private static final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mMoveListener != null) {
                if (msg.what == Constants.MOVE_START) {
                    mMoveListener.startMove();
                } else if (msg.what == Constants.MOVE_FINISH) {
                    mMoveListener.finishMove();
                } else if (msg.what == Constants.MOVE_CANCEL) {
                    mMoveListener.cancleMove();
                }
            }
            // TODO
        }
    };
    //用于处理是否是长按的Runnable
    private final Runnable mLongClickRunnable = new Runnable() {
        @Override
        public void run() {
            //todo
            if (!canDrag) {
                return;
            }
            isDrag = true;
            moved = true;
            handler.sendEmptyMessage(Constants.MOVE_START);
            mVibrator.vibrate(50);
            FirstDragedView.setVisibility(INVISIBLE);//將被点击的该item隐藏
            createDragImageView(mDragBitmap, mDownX, mDownY);//创建镜像view

            mDragBitmap = null;
        }
    };

    /**
     * 创建可以拖动的镜像item的方法
     *
     * @param dragBitmap
     * @param downX
     * @param downY
     */
    private void createDragImageView(Bitmap dragBitmap, int downX, int downY) {

        mWinLayoutParams = new WindowManager.LayoutParams();
        mWinLayoutParams.format = PixelFormat.TRANSLUCENT;//图片之外的其他地方透明
        mWinLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWinLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWinLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top
                - mStatusHeight;
        mWinLayoutParams.alpha = 0.55f; // 透明度


        mWinLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWinLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWinLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(dragBitmap);
        mWindowManager.addView(mDragImageView, mWinLayoutParams);


    }

    /**
     * 移除可移动的镜像item
     */
    private void removeDragImageView() {
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * 拖动item，实现了item位置的更新，item的相互交换以及GridView的自行滚动
     *
     * @param moveX
     * @param moveY
     */
    private void onDragItem(int moveX, int moveY) {
        mWinLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
        mWinLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top
                - mStatusHeight;
        mWindowManager.updateViewLayout(mDragImageView, mWinLayoutParams); // 更新镜像的位置
        onSwapItem(moveX, moveY);
        // GridView自动滚动
        handler.post(mScrollRunnable);
    }

    /**
     * 交换item,并且控制item之间的显示与隐藏效果
     *
     * @param moveX
     * @param moveY
     */
    private void onSwapItem(int moveX, int moveY) {
        //获取我们手指移动到的那个item的position
        int tempPosition = pointToPosition(moveX, moveY);

        //假如tempPosition 改变了并且tempPosition不等于-1,则进行交换
        if (tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION) {
            if (onChanageListener != null) {
                onChanageListener.onChange(mDragPosition, tempPosition);
            }

            getChildAt(tempPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);//拖动到了新的item,新的item隐藏掉
            getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);//之前的item显示出来

            mDragPosition = tempPosition;
        }

    }

    /**
     * 停止拖拽我们将之前隐藏的item显示出来，并将镜像移除
     */
    private void onStopDrag() {
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        mDragAdapter.setHideItem(-1);
        removeDragImageView();
    }

    /**
     * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动 当moveY的值小于向下滚动的边界值，触发GridView自动向下滚动
     * 否则不进行滚动
     */
    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollY;
            if (moveY > mUpScrollBorder) {
                scrollY = speed;
                handler.postDelayed(mScrollRunnable, 25);
            } else if (moveY < mDownScrollBorder) {
                scrollY = -speed;
                handler.postDelayed(mScrollRunnable, 25);
            } else {
                scrollY = 0;
                handler.removeCallbacks(mScrollRunnable);
            }

            //当我们的手指到达GridView向上或者向下滚动的偏移量的时候，可能我们手指没有移动，但是DragGridView在自动的滚动
            //所以我们在这里调用下onSwapItem()方法来交换item
            onSwapItem(moveX, moveY);


            smoothScrollBy(scrollY, 10);
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                //todo
                moveRect.left = mDownX - MOVE_OFFSET;
                moveRect.right = mDownX + MOVE_OFFSET;
                moveRect.top = mDownY - MOVE_OFFSET;
                moveRect.bottom = mDownY + MOVE_OFFSET;
                //根据按下的X,Y坐标获取所点击item的position
                mDragPosition = pointToPosition(mDownX, mDownY);

                if (mDragPosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(ev);
                }

                //使用Handler延迟dragResponseMS执行mLongClickRunnable
                handler.postDelayed(mLongClickRunnable, dragResponseMinute);

                //根据position获取该item所对应的View
                FirstDragedView = getChildAt(mDragPosition - getFirstVisiblePosition());

                mPoint2ItemTop = mDownY - FirstDragedView.getTop();
                mPoint2ItemLeft = mDownX - FirstDragedView.getLeft();

                mOffset2Top = (int) (ev.getRawY() - mDownY);
                mOffset2Left = (int) (ev.getRawX() - mDownX);

                //获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
                mDownScrollBorder = getHeight() / 4;
                //获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
                mUpScrollBorder = getHeight() * 3 / 4;

                //开启mDragItemView绘图缓存
                FirstDragedView.setDrawingCacheEnabled(true);
                //获取mDragItemView在缓存中的Bitmap对象
                mDragBitmap = Bitmap.createBitmap(FirstDragedView.getDrawingCache());
                //这一步很关键，释放绘图缓存，避免出现重复的镜像
                FirstDragedView.destroyDrawingCache();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                if (!isTouchInItem(FirstDragedView, moveX, moveY)) {
                    handler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(mLongClickRunnable);
                handler.removeCallbacks(mScrollRunnable);
                if (moved && getAdapter().getCount() > 0) {
                    handler.sendEmptyMessage(Constants.MOVE_FINISH);
                } else {
                    handler.sendEmptyMessage(Constants.MOVE_CANCEL);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否点击在GridView的item上面
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchInItem(View dragView, int x, int y) {
        if (dragView == null) {
            return false;
        }
        int leftOffset = dragView.getLeft();
        int topOffset = dragView.getTop();
        if (x < leftOffset || x > leftOffset + dragView.getWidth()) {
            return false;
        }

        if (y < topOffset || y > topOffset + dragView.getHeight()) {
            return false;
        }

        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag && mDragImageView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initRecord();
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveX = (int) ev.getX();
                    moveY = (int) ev.getY();
                    onDragItem(moveX, moveY);
                    if (mTrashView != null) {
                        if (inTrash(moveX, moveY)) {
                            mTrashView.setScaleX(1.7f);
                            mTrashView.setScaleY(1.7f);
                        } else {
                            mTrashView.setScaleX(1f);
                            mTrashView.setScaleY(1f);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    onStopDrag();
                    isDrag = false;
                    if (mDeleteListener != null && inTrash(ev.getX(), ev.getY())) {
                        mDeleteListener.onDelete(mDragPosition);
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof GridBaseAdapter) {
            mDragAdapter = (GridBaseAdapter) adapter;
        } else {
            throw new IllegalStateException(
                    "the adapter must be implements DragGridAdapter");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initRecord();
    }
    /**
     * 手指当前处于垃圾桶图标上
     *
     * @param x
     * @param y
     * @return
     */
    private boolean inTrash(float x, float y) {
        x += gridRect.left;
        y += gridRect.top;
        if (x > trashRect.left && x < trashRect.right && y > trashRect.top
                && y < trashRect.bottom) {
            if (handler != null && mScrollRunnable != null) {
                handler.removeCallbacks(mScrollRunnable);
            }
            if (mDragImageView != null) {
                mDragImageView.setScaleX(0.6f);
                mDragImageView.setScaleY(0.6f);
            }
            return true;
        } else {
            if (mDragImageView != null) {
                mDragImageView.setScaleX(1f);
                mDragImageView.setScaleY(1f);
            }
            return false;
        }
    }
    /**
     * 初始化坐标数据
     */
    private void initRecord() {
        gridRect.left = this.getLeft();
        gridRect.bottom = this.getBottom();
        gridRect.right = this.getRight();
        gridRect.top = this.getTop();
        if (mTrashView != null) {
            trashRect.left = mTrashView.getLeft();
            trashRect.right = mTrashView.getRight();
            trashRect.bottom = mTrashView.getBottom();
            trashRect.top = mTrashView.getTop();
        }
    }

    public interface GridBaseAdapter {
        /**
         * 移动时回调
         */
        public void reorderItems(int oldPosition, int newPosition);

        /**
         * 隐藏时回调
         */
        public void setHideItem(int hidePosition);
    }

    public void setOnMoveListener(onMoveListener l) {
        mMoveListener = l;
    }

    public interface onMoveListener {
        void startMove();

        void finishMove();

        void cancleMove();

    }

    public interface onDeleteListener {
        void onDelete(int position);
    }

    public interface OnChanageListener {

        /**
         * 当item交换位置的时候回调的方法，我们只需要在该方法中实现数据的交换即可
         *
         * @param form 开始的position
         * @param to   拖拽到的position
         */
        public void onChange(int form, int to);
    }

    /**
     * 设置回调接口
     *
     * @param onChanageListener
     */
    public void setOnChangeListener(OnChanageListener onChanageListener) {
        this.onChanageListener = onChanageListener;
    }

    public void setOnDeleteListener(onDeleteListener l) {
        this.mDeleteListener = l;
    }

    private class TouchRect {
        int top;
        int bottom;
        int left;
        int right;
    }
}

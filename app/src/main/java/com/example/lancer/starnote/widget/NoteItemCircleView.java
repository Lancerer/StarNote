package com.example.lancer.starnote.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author: Lancer
 * date：2018/9/4
 * des:
 * email:tyk790406977@126.com
 */

public class NoteItemCircleView extends CircleImageView {
    public NoteItemCircleView(Context context) {
        super(context);
    }

    public NoteItemCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteItemCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}

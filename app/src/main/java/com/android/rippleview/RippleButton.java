package com.android.rippleview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by hexiaolei on 2017/6/26.
 * Class Function:
 */

public class RippleButton extends AppCompatButton {

    private RippleHelper mRipplehelper;

    public RippleButton(Context context) {
        super(context);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        mRipplehelper = new RippleHelper(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRipplehelper.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            mRipplehelper.startAnimation(event.getX(), event.getY());
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRipplehelper.detach();
    }

}

package com.android.rippleview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by hexiaolei on 2017/6/28.
 * Class Function: 水纹动画的实现类
 */

public class RippleHelper {

    private View mView = null;

    //水波动画
    private Point mClickPoint = new Point();
    private Paint mRipplePaint = new Paint();
    private DecelerateInterpolator mInterpolator = new DecelerateInterpolator(2);

    private ValueAnimator mRippleValueAnimator = null;
    private boolean mRippleAnimationRunning = false;//是否正在动画
    private float mRippleFactor = 0;//动画进度
    private float mRippleRadius = 0;//圆半径长


    private static final int RIPPLE_ANIMATION_DURATION = 350;//动画时长
    private static final float RIPPLE_START_FACTOR = 0.1f;//起始mRippleFactor值

    public RippleHelper(View v) {
        mView = v;
        mRipplePaint.setColor(Color.parseColor("#F0F0F0"));
        mRipplePaint.setAlpha(100);
    }

    /**
     * 实际绘制
     *
     * @param canvas
     */
    public void onDraw(Canvas canvas) {
        if (mRippleAnimationRunning) {
            if (mRippleFactor == 1) {
                mRippleAnimationRunning = false;
            } else {
                canvas.drawCircle(mClickPoint.x, mClickPoint.y, mRippleRadius * mRippleFactor, mRipplePaint);
            }
        }
    }

    /**
     * 启动动画
     *
     * @param x
     * @param y
     */
    public void startAnimation(float x, float y) {
        if (mRippleValueAnimator != null && mRippleValueAnimator.isRunning()) {
            mRippleValueAnimator.end();
        }

        if (mRippleValueAnimator == null) {
            mRippleValueAnimator = ValueAnimator.ofFloat(RIPPLE_START_FACTOR, 1);
            mRippleValueAnimator.setDuration(RIPPLE_ANIMATION_DURATION);
            mRippleValueAnimator.setInterpolator(mInterpolator);
            mRippleValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mRippleAnimationRunning = true;
                    mRippleFactor = 0;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mRippleAnimationRunning = false;
                    mRippleFactor = 0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mRippleAnimationRunning = false;
                    mRippleFactor = 0;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    mRippleAnimationRunning = false;
                    mRippleFactor = 0;
                }
            });
            mRippleValueAnimator.addUpdateListener(animation -> {
                mRippleFactor = (float) animation.getAnimatedValue();
                mView.invalidate();
            });
        }

        mClickPoint.set((int) x, (int) y);
        mRippleRadius = (float) calculateRippleRadius();

        mRippleValueAnimator.start();
    }

    /**
     * 计算圆的最大半径长
     *
     * @return
     */
    private double calculateRippleRadius() {
        if (mClickPoint.x <= mView.getWidth() / 2 && mClickPoint.y <= mView.getHeight() / 2) {//象限第一区域
            return Math.sqrt(Math.pow(mView.getWidth() - mClickPoint.x, 2) + Math.pow(mView.getHeight() - mClickPoint.y, 2));
        } else if (mClickPoint.x >= mView.getWidth() / 2 && mClickPoint.y <= mView.getHeight() / 2) {//第二区域
            return Math.sqrt(Math.pow(mClickPoint.x, 2) + Math.pow(mView.getHeight() - mClickPoint.y, 2));
        } else if (mClickPoint.x <= mView.getWidth() / 2 && mClickPoint.y >= mView.getHeight() / 2) {//第三区域
            return Math.sqrt(Math.pow(mView.getWidth() - mClickPoint.x, 2) + Math.pow(mClickPoint.y, 2));
        } else {//第四区域
            return Math.sqrt(Math.pow(mClickPoint.x, 2) + Math.pow(mClickPoint.y, 2));
        }
    }

    public void detach() {
        if (mRippleValueAnimator != null && mRippleValueAnimator.isRunning()) {
            mRippleValueAnimator = null;
        }
        mView = null;
    }

}

package com.ximsfei.rush.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ximsfei on 17-2-18.
 */

public class RushView extends View {

    private static final int DEFAULT_BORDER_NUM = 4;
    private static final int DEFAULT_RUSH_DURATION = 110;
    private static final int SIX_RUSH_DURATION = 80;
    private static final int DEFAULT_DURATION = 1200;
    private static final int SIX_DURATION = 1600;
    private boolean mAnimStarted = false;

    private int mHeight;
    private int mWidth;

    private Paint areaPaint;

    private float x, y;
    private float mRadius;

    private float angle = (float) ((2 * Math.PI) / DEFAULT_BORDER_NUM);

    private int[] areaColor = {android.R.color.holo_red_light,
            android.R.color.holo_blue_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_orange_light};
    private double mTopValue = 0;
    private int mBorderNum = DEFAULT_BORDER_NUM;


    public RushView(Context context) {
        super(context);
        init(context);
    }

    public RushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RushView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRushViewClicked();
            }
        });
    }

    public synchronized void onRushViewClicked() {
        if (mAnimStarted) {
            return;
        }
        mAnimStarted = true;
        final ObjectAnimator anim = ObjectAnimator.ofFloat(RushView.this, "rotation",
                getRotation(), getRotation() + 360 / mBorderNum);
        anim.setDuration(getRushDuration());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimStarted = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    private int getRushDuration() {
        return mBorderNum == DEFAULT_BORDER_NUM ? DEFAULT_RUSH_DURATION : SIX_RUSH_DURATION;
    }

    public int getDuration() {
        return mBorderNum == DEFAULT_BORDER_NUM ? DEFAULT_DURATION : SIX_DURATION;
    }

    public double getTopValue() {
        return mTopValue;
    }

    public boolean hit(int indicator) {
        int rotation = (int) (getRotation() % 360);
        int index = (mBorderNum - rotation * mBorderNum / 360) % mBorderNum;
        Log.e("pengfeng", "rotation = " + rotation + ", indicator = " + indicator + ", index = " + index);
        return index == indicator;
    }

    public int getAreaColor(int area) {
        return areaColor[area];
    }

    public void initDefault() {
        mBorderNum = DEFAULT_BORDER_NUM;
        angle = (float) (2 * Math.PI) / DEFAULT_BORDER_NUM;
    }

    public void initSixBorder() {
        mBorderNum = 6;
        Log.e("pengfeng", "mBorderNum 1 = " + mBorderNum);
        angle = (float) (2 * Math.PI) / 6;
    }

    public int getBorderNum() {
        return mBorderNum;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = w / (mBorderNum == DEFAULT_BORDER_NUM ? 3.3f : 4);
        mTopValue = mHeight / 2 + mRadius * Math.cos(Math.PI / mBorderNum);
        Log.e("pengfeng", "mBorderNum 2 = " + mBorderNum);
        Log.e("pengfeng", "mHeight = " + mHeight + "，top value = " + mTopValue + "， mRadius = " + mRadius + ", Math.cos(Math.PI / (mBorderNum * 2)) " + mRadius * Math.cos(Math.PI / (mBorderNum * 2)));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * 2 / 3, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始化画笔
        initPaint();
        //画布移到中心点
        canvas.translate(mWidth / 2, mHeight / 2);
        drawArea(canvas);
    }

    private void initPaint() {
        areaPaint = new Paint();
        areaPaint.setAntiAlias(true);
        areaPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void drawArea(Canvas canvas) {
        Path path = new Path();
        for (int i = 1; i <= mBorderNum; i++) {
            int color = ResourcesCompat.getColor(getResources(), areaColor[i - 1], getResources().newTheme());
            areaPaint.setColor(color);
            path.reset();
            x = (float) (Math.cos(i % mBorderNum * angle
                    + angle * 3 / (mBorderNum == DEFAULT_BORDER_NUM ? 2 : 1)) * mRadius);
            y = (float) (Math.sin(i % mBorderNum * angle
                    + angle * 3 / (mBorderNum == DEFAULT_BORDER_NUM ? 2 : 1)) * mRadius);
            float x1 = (float) (Math.cos((i + 1) % mBorderNum * angle
                    + angle * 3 / (mBorderNum == DEFAULT_BORDER_NUM ? 2 : 1)) * mRadius);
            float y1 = (float) (Math.sin((i + 1) % mBorderNum * angle
                    + angle * 3 / (mBorderNum == DEFAULT_BORDER_NUM ? 2 : 1)) * mRadius);
            Log.e("pengfeng", "x = " + x + ", y = " + y + ", x1 = " + x1 + ", y1 = " + y1);
            path.lineTo(x, y);
            path.lineTo(x1, y1);
            canvas.drawPath(path, areaPaint);

            path.close();
        }
    }
}

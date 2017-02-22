package com.ximsfei.rush.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ximsfei on 17-2-18.
 */

public class RushView extends View {

    //n边形
    private int n = 6;

    //-------------View相关-------------
    //View自身的宽和高
    private int mHeight;
    private int mWidth;

    //-------------画笔相关-------------
    //区域的画笔
    private Paint areaPaint;

    //-------------多边形相关-------------
    //n边形顶点坐标
    private float x, y;
    private float mRadius;
    //n边形角度
    private float angle = (float) ((2 * Math.PI) / n);

    //-------------颜色相关-------------
    //区域颜色
    private int[] areaColor = {android.R.color.holo_red_light,
            android.R.color.holo_blue_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_purple,
            android.R.color.holo_orange_light};


    public RushView(Context context) {
        super(context);
    }

    public RushView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RushView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = w / 3;
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
        for (int i = 1; i <= n; i++) {
            int color = ResourcesCompat.getColor(getResources(), areaColor[i - 1], getResources().newTheme());
            areaPaint.setColor(color);
            path.reset();
            x = (float) (Math.cos(i % n * angle) * mRadius);
            y = (float) (Math.sin(i % n * angle) * mRadius);
            float x1 = (float) (Math.cos((i + 1) % n * angle) * mRadius);
            float y1 = (float) (Math.sin((i + 1) % n * angle) * mRadius);
            path.lineTo(x, y);
            path.lineTo(x1, y1);
            canvas.drawPath(path, areaPaint);

            path.close();
        }
    }
}

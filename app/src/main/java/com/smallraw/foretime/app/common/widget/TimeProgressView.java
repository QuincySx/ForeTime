package com.smallraw.foretime.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class TimeProgressView extends View {
    public TimeProgressView(Context context) {
        this(context, null);
    }

    public TimeProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int mWidth;
    private int mHeight;

    private float mProgress = 0.0F;

    @ColorInt
    private int mProgressLowerColor = Color.parseColor("#D9D9D9");
    private float mProgressLowerHeight;

    @ColorInt
    private int mProgressColor = Color.parseColor("#3DCF7A");
    private float mProgressHeight;

    private Paint mProgressLowerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void init() {
        mProgressHeight = mProgressLowerHeight = AutoSizeUtils.dp2px(getContext(), 3);

        mProgressLowerPaint.setStrokeWidth(mProgressLowerHeight);
        mProgressLowerPaint.setColor(mProgressLowerColor);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mProgressPaint.setStrokeWidth(mProgressHeight);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, mProgressLowerHeight, mWidth, mProgressLowerHeight, mProgressLowerPaint);
        canvas.drawLine(0, mProgressHeight, mWidth * mProgress, mProgressHeight, mProgressPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = getDefWidth();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = getDefWidth();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    /**
     * 获取控件默认高度
     *
     * @return 高度
     */
    private int getDefWidth() {
        return (int) (Math.max(mProgressHeight, mProgressHeight) * 2);
    }

    /**
     * 设置圆圈进度
     *
     * @param progress 0-1
     */
    public void setProgress(float progress) {
        mProgress = progress;
        postInvalidate();
    }

    /**
     * 设置进度条颜色
     *
     * @param color 颜色
     */
    public void setProgressColor(@ColorInt int color) {
        mProgressColor = color;
        postInvalidate();
    }
}

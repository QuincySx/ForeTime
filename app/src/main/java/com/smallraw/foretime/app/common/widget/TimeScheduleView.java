package com.smallraw.foretime.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class TimeScheduleView extends View {
  public TimeScheduleView(Context context) {
    this(context, null);
  }

  public TimeScheduleView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TimeScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private int mWidth;
  private int mHeight;

  private float mProgress = 0.0F;

  private Path mPath = new Path();
  private Path mPathDraw = new Path();
  private PathMeasure mPathMeasure = new PathMeasure();

  @ColorInt
  private int mCircleLowerColor = Color.parseColor("#E9E9E9");
  private float mCircleLowerLineWidth;
  private float mCircleLowerRadius;

  @ColorInt
  private int mProgressColor = Color.parseColor("#5165FF");
  private float mProgressCircleWidth;
  private float mProgressCircleRadius;

  @ColorInt
  private int mTextColor = Color.parseColor("#333333");
  private float mTextSize;
  private String mTextContent = "00:00";
  private Rect mTextRect = new Rect();

  private Paint mPaintCircleLower = new Paint(Paint.ANTI_ALIAS_FLAG);
  private Paint mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
  private Paint mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);

  private void init() {
    mCircleLowerLineWidth = AutoSizeUtils.dp2px(getContext(), 2);
    mProgressCircleWidth = AutoSizeUtils.dp2px(getContext(), 7);
    mProgressCircleRadius = mCircleLowerRadius = AutoSizeUtils.dp2px(getContext(), 85);
    mTextSize = AutoSizeUtils.sp2px(getContext(), 36);

    mPaintCircleLower.setStrokeWidth(mCircleLowerLineWidth);
    mPaintCircleLower.setColor(mCircleLowerColor);
    mPaintCircleLower.setStyle(Paint.Style.STROKE);

    mPaintCircle.setStrokeWidth(mProgressCircleWidth);
    mPaintCircle.setColor(mProgressColor);
    mPaintCircle.setStyle(Paint.Style.STROKE);
    mPaintCircle.setStrokeCap(Paint.Cap.ROUND);

    mPaintText.setTextSize(mTextSize);
    mPaintText.setColor(mTextColor);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mWidth = w;
    mHeight = h;

    mPath.addCircle(mWidth / 2, mHeight / 2, mProgressCircleRadius, Path.Direction.CW);
    mPathMeasure.setPath(mPath, false);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawCircle(mWidth / 2, mHeight / 2, mProgressCircleRadius, mPaintCircleLower);

    Log.e("====e==",mProgress+"  sss "+mProgress * mPathMeasure.getLength());
    canvas.save();
    canvas.rotate(-90, mWidth / 2, mHeight / 2);
    mPathDraw.reset();
    mPathMeasure.getSegment(0, mProgress * mPathMeasure.getLength(), mPathDraw, true);
    canvas.drawPath(mPathDraw, mPaintCircle);
    canvas.restore();

    mPaintText.getTextBounds(mTextContent, 0, mTextContent.length(), mTextRect);
    canvas.drawText(mTextContent, mWidth / 2 - mTextRect.width() / 2, mWidth / 2 + mTextRect.height() / 2, mPaintText);
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
    return (int) (Math.max(mProgressCircleRadius, mCircleLowerRadius) * 2 + Math.max(mProgressCircleWidth, mProgressCircleWidth) * 2);
  }

  /**
   * 设置圆圈进度
   *
   * @param progress 0-1
   */
  public void setProgress(float progress) {
    mProgress = progress;
  }

  /**
   * 设置圆圈颜色
   *
   * @param color 颜色
   */
  public void setProgressColor(@ColorInt int color) {
    mProgressColor = color;
  }

  /**
   * 设置圆圈中间字体显示
   *
   * @param text 字体内容
   */
  public void setText(String text) {
    mTextContent = text;
  }
}

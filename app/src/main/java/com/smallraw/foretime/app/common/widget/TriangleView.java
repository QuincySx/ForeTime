package com.smallraw.foretime.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class TriangleView extends View {
  private float mHeight;
  private float mWidth;

  private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private Path mPath = new Path();

  public TriangleView(Context context) {
    this(context, null);
  }

  public TriangleView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mHeight = AutoSizeUtils.dp2px(getContext(), 14);
    mWidth = AutoSizeUtils.dp2px(getContext(), 10);

    mPaint.setColor(Color.parseColor("#FFFFFF"));
    mPaint.setStyle(Paint.Style.FILL);

    mPath.moveTo(0, mHeight);
    mPath.lineTo(mWidth, mHeight);
    mPath.lineTo(mWidth / 2, 0);
    mPath.close();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawPath(mPath, mPaint);
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
      result = getDefHeight();
      if (mode == MeasureSpec.AT_MOST) {
        result = Math.min(result, size);
      }
    }
    return result;
  }

  private int getDefHeight() {
    return (int) mHeight;
  }

  private int getDefWidth() {
    return (int) mWidth;
  }
}

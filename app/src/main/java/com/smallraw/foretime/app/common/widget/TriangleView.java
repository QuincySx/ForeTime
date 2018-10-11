package com.smallraw.foretime.app.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.smallraw.foretime.app.R;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class TriangleView extends View {
  private float mHeight;
  private float mWidth;
  int mTriangleDirection = 0; // 0 top 1 bottom

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

    TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TriangleView, defStyleAttr, 0);
    mTriangleDirection = typedArray.getInt(R.styleable.TriangleView_triangleDirection, 0);
    typedArray.recycle();
    init();
  }

  private void init() {
    mHeight = AutoSizeUtils.dp2px(getContext(), 12);
    mWidth = AutoSizeUtils.dp2px(getContext(), 10);

    mPaint.setColor(Color.parseColor("#FFFFFF"));
    mPaint.setStyle(Paint.Style.FILL);

    if (mTriangleDirection == 0) {
      mPath.moveTo(0, mHeight);
      mPath.lineTo(mWidth, mHeight);
      mPath.lineTo(mWidth / 2, 0);
      mPath.close();
    } else {
      mPath.moveTo(0, 0);
      mPath.lineTo(mWidth, 0);
      mPath.lineTo(mWidth / 2, mHeight);
      mPath.close();
    }
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
      result = getDefHeight();
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

  private int getDefHeight() {
    return (int) mHeight;
  }

  private int getDefWidth() {
    return (int) mWidth;
  }
}

package com.smallraw.support.switchcompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SwitchView extends View {
  private boolean isPressed;
  private boolean isChecked;
  private float mProcess = 0;
  private int mWidth = 0;
  private int mHeight = 0;
  private float mTrackWidth;
  private float mTrackHeight;
  private float mTrackButtonWidth;

  RectF leftRectF = new RectF(0, 0, 0, 0);
  RectF rightRectF = new RectF(0, 0, 0, 0);
  RectF trackRectF = new RectF(0, 0, 0, 0);

  private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  private Bitmap mBitmapButton;
  private Bitmap mBitmapOpen;
  private Bitmap mBitmapClosed;

  public SwitchView(Context context) {
    this(context, null);
  }

  public SwitchView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    isPressed = false;
    isChecked = true;
    mProcess = 0.3f;
    setLayerType(LAYER_TYPE_SOFTWARE, null);
    initBitmap();

    mPaint.setColor(Color.parseColor("#CCCCCC"));
  }

  private void initBitmap() {
    mBitmapButton = BitmapFactory.decodeResource(getResources(), R.drawable.view_bg_custom_button);
    mBitmapOpen = BitmapFactory.decodeResource(getResources(), R.drawable.view_bg_custom_open);
    mBitmapClosed = BitmapFactory.decodeResource(getResources(), R.drawable.view_bg_custom_closed);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mWidth = w;
    mHeight = h;

    mTrackWidth = w * 0.8f;
    float www = (w - mTrackWidth) / 2;

    mTrackHeight = h * 0.8f;
    float hhh = (h - mTrackHeight) / 2;

    mTrackButtonWidth = w * 0.9f;

    trackRectF.set(www, hhh, mTrackWidth + www, mTrackHeight + hhh);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    leftRectF.set(trackRectF.left, trackRectF.top, trackRectF.right + mTrackWidth * mProcess, trackRectF.bottom);
    rightRectF.set(trackRectF.left + mTrackWidth * mProcess, trackRectF.top, trackRectF.right, trackRectF.bottom);


    if (isChecked) {
      canvas.save();
      canvas.clipRect(leftRectF);
      canvas.drawBitmap(mBitmapOpen, trackRectF.left, trackRectF.top, mPaint);
      canvas.restore();

      canvas.save();
      canvas.clipRect(rightRectF);
      canvas.drawBitmap(mBitmapClosed, trackRectF.left, trackRectF.top, mPaint);
      canvas.restore();
    } else {
      canvas.save();
      canvas.clipRect(leftRectF);
      canvas.drawBitmap(mBitmapClosed, trackRectF.left, trackRectF.top, mPaint);
      canvas.restore();

      canvas.save();
      canvas.clipRect(rightRectF);
      canvas.drawBitmap(mBitmapOpen, trackRectF.left, trackRectF.top, mPaint);
      canvas.restore();
    }
      canvas.drawBitmap(mBitmapButton, trackRectF.left + mTrackWidth * mProcess - mTrackButtonWidth / 2, 0, mPaint);

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
    return dip2px(30f);
  }

  private int getDefWidth() {
    return dip2px(50f);
  }

  private int dip2px(float dpValue) {
    final float scale = getContext().getResources().getDisplayMetrics().density;
    return (int) ((dpValue * scale) + 0.5f);
  }
}

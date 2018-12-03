package com.smallraw.foretime.app.common.widget.colorRecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class ColorItemNormalDrawable extends Drawable {
  private float mPadding = 0;
  @ColorInt
  private int mColor = Color.parseColor("#139EED");
  private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  public ColorItemNormalDrawable(Context context) {
    mPadding=AutoSizeUtils.dp2px(context.getApplicationContext(),1);
  }

  public int getColor() {
    return mColor;
  }

  public void setColor(int color) {
    mColor = color;
    invalidateSelf();
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    Rect mRect = getBounds();
    int width = mRect.right - mRect.left;
    int height = mRect.bottom - mRect.top;

    mPaint.setColor(mColor);
    mPaint.setStyle(Paint.Style.FILL);
    int radius = Math.min(width, height) / 2;
    canvas.drawCircle(width / 2, height / 2, radius - mPadding, mPaint);
  }

  @Override
  public void setAlpha(int alpha) {

  }

  @Override
  public void setColorFilter(@Nullable ColorFilter colorFilter) {

  }

  @Override
  public int getOpacity() {
    return PixelFormat.OPAQUE;
  }
}
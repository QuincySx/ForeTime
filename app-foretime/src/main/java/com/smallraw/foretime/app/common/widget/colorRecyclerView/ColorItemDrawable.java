package com.smallraw.foretime.app.common.widget.colorRecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;

public class ColorItemDrawable extends StateListDrawable {
  private ColorItemNormalDrawable mColorItemNormalDrawable;
  private ColorItemSelectDrawable mColorItemSelectDrawable;
  private int mColor = Color.parseColor("#139EED");

  public ColorItemDrawable(Context context) {
    mColorItemNormalDrawable = new ColorItemNormalDrawable(context);
    mColorItemSelectDrawable = new ColorItemSelectDrawable(context);

    addState(new int[]{android.R.attr.state_pressed}, mColorItemSelectDrawable);
    addState(new int[]{android.R.attr.state_selected}, mColorItemSelectDrawable);

    addState(new int[]{}, mColorItemNormalDrawable);
  }

  public void setColor(int color) {
    mColor = color;
    mColorItemNormalDrawable.setColor(mColor);
    mColorItemSelectDrawable.setColor(mColor);
    invalidateSelf();
  }
}

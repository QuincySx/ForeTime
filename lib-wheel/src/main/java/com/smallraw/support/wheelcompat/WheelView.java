package com.smallraw.support.wheelcompat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class WheelView extends RecyclerView {

  public WheelView(@NonNull Context context) {
    super(context);
    init();
  }

  public WheelView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public WheelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
    linearSnapHelper.attachToRecyclerView(this);
  }


}

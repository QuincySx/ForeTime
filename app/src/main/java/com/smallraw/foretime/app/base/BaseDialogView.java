package com.smallraw.foretime.app.base;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.smallraw.foretime.app.R;

import me.jessyan.autosize.utils.ScreenUtils;

public abstract class BaseDialogView extends Dialog {
  private WindowManager mWindowManager;

  public BaseDialogView(Context context) {
    super(context);
    init();
  }

  public BaseDialogView(Context context, boolean cancelable, OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
    init();
  }

  public BaseDialogView(Context context, int themeResId) {
    super(context, themeResId);
    init();
  }

  private void init() {
    mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    setContentView(R.layout.base_dialog);
    FrameLayout rootView = findViewById(R.id.root_view);
    rootView.addView(setRootView());
    initView();
  }

  protected abstract void initView();

  /**
   * 设置显示的主视图
   *
   * @return
   */
  protected abstract View setRootView();

  public void showAtViewDown(View view) {
    showAtView(view, 0, false);
  }

  public void showAtViewDown(View view, int padding) {
    showAtView(view, padding, false);
  }

  public void showAtViewUp(View view, int padding) {
    showAtView(view, padding, true);
  }

  public void showAtViewUp(View view) {
    showAtView(view, 0, true);
  }

  public void showAtViewAuto(View view) {
    showAtViewAuto(view, 0);
  }

  public void showAtViewAuto(View view, int padding) {
    DisplayMetrics dm = new DisplayMetrics();
    mWindowManager.getDefaultDisplay().getMetrics(dm);
    int screenHeight = dm.heightPixels;

    int location[] = new int[2];
    view.getLocationOnScreen(location);
    int viewY = location[1];

    boolean isUp = false;
    if (viewY > screenHeight / 2) {
      isUp = true;
    }
    showAtView(view, padding, isUp);
  }

  public void showAtView(View view, int padding, boolean isUp) {
    int location[] = new int[2];
    view.getLocationOnScreen(location);
    int viewX = location[0];
    int viewY = location[1];
    int viewWidthHalf = view.getWidth() / 2 + view.getPaddingLeft();
    int viewHeight = view.getMeasuredHeight();
    getWindow().setDimAmount(0.3f);
    show();

    ViewGroup parentView = findViewById(R.id.patent_view);
    View arrow;
    if (isUp) {
      arrow = parentView.findViewById(R.id.doalog_arrow_bottom);
    } else {
      arrow = parentView.findViewById(R.id.doalog_arrow_up);
    }

    arrow.setVisibility(View.VISIBLE);
    arrow.post(new Runnable() {
      @Override
      public void run() {
        ViewGroup.LayoutParams layoutParams = arrow.getLayoutParams();

        ViewGroup.MarginLayoutParams marginParams;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
          marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
        } else {
          marginParams = new ViewGroup.MarginLayoutParams(layoutParams);
        }


        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        int windowViewX = (screenWidth - parentView.getMeasuredWidth()) / 2;

        int arrowWidthHalf = arrow.getWidth() / 2;

        marginParams.setMargins(viewX - windowViewX + viewWidthHalf - arrowWidthHalf, 0, 0, 0);

        arrow.setLayoutParams(marginParams);

        if (isUp) {
          int screenHeight = dm.heightPixels - viewY;
          windowDeploy(0, screenHeight + padding, Gravity.BOTTOM);
        } else {
          int statusBarHeight = ScreenUtils.getStatusBarHeight();
          windowDeploy(0, viewY - statusBarHeight + viewHeight + padding, Gravity.TOP);
        }
      }
    });
  }

  public void windowDeploy(int x, int y, int gravity) {
    Window window = getWindow();
    if (window == null) {
      return;
    }
    window.setWindowAnimations(R.style.baseDialogWindowAnim);
    window.setBackgroundDrawableResource(R.color.vifrification);
    WindowManager.LayoutParams wl = window.getAttributes();
    wl.x = x;
    wl.y = y;
    wl.gravity = gravity;
    window.setAttributes(wl);
  }


}

package com.smallraw.foretime.app.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.common.widget.TriangleView;

public class BaseDialogView extends Dialog {
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
    setContentView(R.layout.base_dialog);
  }

  public void showAtViewDown(View view) {
    Log.e("=====", "sdfsdfsdf");
    int location[] = new int[2];
    view.getLocationOnScreen(location);
    int viewX = location[0];
    int viewY = location[1];
    int viewWidthHalf = view.getMeasuredWidth() / 2;

    ViewGroup parentView = findViewById(R.id.patent_view);
    TriangleView arrow = parentView.findViewById(R.id.doalog_arrow_up);
    arrow.setVisibility(View.VISIBLE);
    ViewGroup.LayoutParams layoutParams = arrow.getLayoutParams();

    ViewGroup.MarginLayoutParams marginParams;
    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
      marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
    } else {
      marginParams = new ViewGroup.MarginLayoutParams(layoutParams);
    }

    int[] parentLocation = new int[2];
    parentView.getLocationOnScreen(parentLocation);

    int windowViewX = parentLocation[0];
    int windowViewY = parentLocation[1];

    int arrowWidthHalf = arrow.getMeasuredWidth() / 2;

    Log.e("=====","viewWidthHalf "+viewWidthHalf+"    arrowWidthHalf "+arrowWidthHalf );
    marginParams.setMargins(viewX - windowViewX - viewWidthHalf + arrowWidthHalf, 0, 0, 0);
    arrow.setLayoutParams(marginParams);

    show();
  }

  public void showAtViewUp(View view) {

  }
}

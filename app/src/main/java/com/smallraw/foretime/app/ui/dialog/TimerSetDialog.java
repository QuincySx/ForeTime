package com.smallraw.foretime.app.ui.dialog;

import android.content.Context;
import android.view.View;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;

public class TimerSetDialog extends BaseDialogView {

  public TimerSetDialog(Context context) {
    super(context);
  }

  @Override
  protected View setRootView() {
    View inflate = getLayoutInflater().inflate(R.layout.dialog_timer_setting, null, false);
    return inflate;
  }
}

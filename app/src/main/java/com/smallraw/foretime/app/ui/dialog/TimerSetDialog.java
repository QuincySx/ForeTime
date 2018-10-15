package com.smallraw.foretime.app.ui.dialog;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;

public class TimerSetDialog extends BaseDialogView {

  public TimerSetDialog(Context context) {
    super(context);
  }

  private ConstraintLayout mLayoutTime;
  private TextView mTvTimeText;
  private TextView mTvTimeHint;
  private ImageView mIvTimeArrow;
  private RecyclerView mRvTime;

  private boolean isShowTime = false;

  @Override
  protected View setRootView() {
    View inflate = getLayoutInflater().inflate(R.layout.dialog_timer_setting, null, false);
    return inflate;
  }

  @Override
  protected void initView() {
    mLayoutTime = findViewById(R.id.layout_time);
    mTvTimeText = findViewById(R.id.tv_time_text);
    mTvTimeHint = findViewById(R.id.tv_time_hint);
    mIvTimeArrow = findViewById(R.id.iv_time_arrow);
    mRvTime = findViewById(R.id.recycler_view_time);

    mTvTimeText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mTvTimeText.setVisibility(View.GONE);
        mTvTimeHint.setVisibility(View.GONE);
        mIvTimeArrow.setVisibility(View.GONE);
        mRvTime.setVisibility(View.VISIBLE);
        isShowTime = true;
      }
    });
  }
}

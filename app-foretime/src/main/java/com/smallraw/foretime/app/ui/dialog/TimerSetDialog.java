package com.smallraw.foretime.app.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;
import com.smallraw.foretime.app.ui.decoration.SpacesItemDecoration;
import com.smallraw.foretime.app.ui.harvestToday.HarvestTodayActivity;
import com.smallraw.foretime.app.ui.setting.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class TimerSetDialog extends BaseDialogView {
  private ConstraintLayout mLayoutTime;
  private TextView mTvTimeText;
  private TextView mTvTimeHint;
  private ImageView mIvTimeArrow;
  private RecyclerView mRvTime;
  private View mLayoutMoreSetting;
  private View mLayoutShare;
  private List<Integer> mTimeList;
  private MyAdapter mMyAdapter;

  private boolean isShowTime = false;

  public TimerSetDialog(Context context) {
    super(context);
  }

  @Override
  protected View setRootView() {
    View inflate = getLayoutInflater().inflate(R.layout.dialog_timer_setting, null, false);
    return inflate;
  }

  @Override
  protected void initView() {
    mTimeList = new ArrayList<>();
    mTimeList.add(10);
    mTimeList.add(20);
    mTimeList.add(30);
    mTimeList.add(40);
    mTimeList.add(50);

    mLayoutShare = findViewById(R.id.layoutShare);
    mLayoutTime = findViewById(R.id.layout_time);
    mTvTimeText = findViewById(R.id.tv_time_text);
    mTvTimeHint = findViewById(R.id.tv_time_hint);
    mIvTimeArrow = findViewById(R.id.iv_time_arrow);
    mLayoutMoreSetting = findViewById(R.id.layout_more_setting);
    mRvTime = findViewById(R.id.recycler_view_time);
    mRvTime.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mRvTime.addItemDecoration(new SpacesItemDecoration(AutoSizeUtils.dp2px(getContext(), 27)));
    mMyAdapter = new MyAdapter(mTimeList, new MyAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(View view, int time, int position) {
        mMyAdapter.select(position);
        mTvTimeText.setVisibility(View.VISIBLE);
        mTvTimeHint.setVisibility(View.VISIBLE);
        mIvTimeArrow.setVisibility(View.VISIBLE);
        mRvTime.setVisibility(View.GONE);
        isShowTime = false;
      }
    });
    mRvTime.setAdapter(mMyAdapter);

    mMyAdapter.select(2);

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

    mLayoutMoreSetting.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        getContext().startActivity(intent);
        dismiss();
      }
    });

    mLayoutShare.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getContext(), HarvestTodayActivity.class);
        getContext().startActivity(intent);
        dismiss();
      }
    });

  }

  public static class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Integer> mTimeList = new ArrayList<>(4);
    private OnItemClickListener mOnItemClickListener;
    private int mSelectPosition = -1;

    public MyAdapter(List<Integer> timeList, OnItemClickListener listener) {
      mTimeList.clear();
      mTimeList.addAll(timeList);
      this.mOnItemClickListener = listener;
    }

    public void select(int position) {
      mSelectPosition = position;
      notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view = View.inflate(viewGroup.getContext(), R.layout.item_select_time, null);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
      if (viewHolder.getAdapterPosition() == mSelectPosition) {
        viewHolder.mContent.setSelected(true);
      } else {
        viewHolder.mContent.setSelected(false);
      }

      viewHolder.mContent.setText(mTimeList.get(i) + " 分钟");
      viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, mTimeList.get(i), viewHolder.getAdapterPosition());
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return mTimeList.size();
    }

    public interface OnItemClickListener {
      void onItemClick(View view, int time, int position);
    }
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView mContent;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      mContent = itemView.findViewById(R.id.tv_time_content);
    }
  }
}

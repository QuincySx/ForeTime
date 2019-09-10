package com.smallraw.foretime.app.ui.tomatoBell;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smallraw.foretime.app.App;
import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;
import com.smallraw.foretime.app.config.ConfigManagerKt;
import com.smallraw.foretime.app.ui.decoration.SpacesItemDecoration;
import com.smallraw.foretime.app.ui.harvestToday.HarvestTodayActivity;
import com.smallraw.foretime.app.ui.tomatoSetting.TomatoSettingActivity;
import com.smallraw.support.switchcompat.SwitchButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * 固定时间选择器
 */
public class TomatoSettingDialog extends BaseDialogView {
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
    private SwitchButton swbMusic;
    private SwitchButton swbImmerse;

    private OnChangeListener onChangeListener;

    public TomatoSettingDialog(Builder builder) {
        super(builder);
        onChangeListener = builder.onChangeListener;
    }

    private TomatoSettingDialog(Context context) {
        super(context);
    }

    @Override
    protected View setRootView() {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_tomato_timer_setting, null, false);
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

        swbMusic = findViewById(R.id.swb_music);
        swbImmerse = findViewById(R.id.swb_immerse);
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
                App.getInstance().getCalendarConfig().setFocusTime(time * 60 * 1000L);
                mTvTimeText.setText(time + " 分钟");
                mTvTimeText.setVisibility(View.VISIBLE);
                mTvTimeHint.setVisibility(View.VISIBLE);
                mIvTimeArrow.setVisibility(View.VISIBLE);
                mRvTime.setVisibility(View.GONE);
                isShowTime = false;
                onChangeListener.onFocusTimeChange();
            }
        });
        mRvTime.setAdapter(mMyAdapter);

        Double l = App.getInstance().getCalendarConfig().getFocusTime() / 1000 / 60 / 10.0 - 1;
        if (l % 1 == 0) {
            mMyAdapter.select(l.intValue());
        }

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
                Intent intent = new Intent(getContext(), TomatoSettingActivity.class);
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
        mTvTimeText.setText((App.getInstance().getCalendarConfig().getFocusTime() / 1000 / 60) + " 分钟");

        swbMusic.setChecked(App.getInstance().getMusicConfig().getPlayMusic());
        swbImmerse.setChecked(App.getInstance().getCalendarConfig().getAutomatic());

        swbMusic.setOnCheckedChangeListener((buttonView, isChecked) ->
                App.getInstance().getMusicConfig().setPlayMusic(isChecked)
        );
        swbImmerse.setOnCheckedChangeListener((buttonView, isChecked) ->
                App.getInstance().getCalendarConfig().setAutomatic(isChecked)
        );
    }

    @Override
    public void dismiss() {
        App.getInstance().getAppExecutors().diskIO().execute(() -> {
            ConfigManagerKt.saveConfig(App.getInstance().getCalendarConfig());
            ConfigManagerKt.saveConfig(App.getInstance().getMusicConfig());
        });
        super.dismiss();
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
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, mTimeList.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            if (viewHolder.getAdapterPosition() == mSelectPosition) {
                viewHolder.mContent.setSelected(true);
            } else {
                viewHolder.mContent.setSelected(false);
            }

            viewHolder.mContent.setText(mTimeList.get(i) + " 分钟");
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

    public interface OnChangeListener {
        void onFocusTimeChange();
    }

    public static class Builder extends BaseDialogView.Builder {
        private OnChangeListener onChangeListener;

        public Builder(@NotNull Context context) {
            super(context);
        }

        public TomatoSettingDialog.Builder setOnChangeListener(OnChangeListener listener) {
            this.onChangeListener = listener;
            return this;
        }

        @NotNull
        @Override
        public TomatoSettingDialog build() {
            return new TomatoSettingDialog(this);
        }
    }
}

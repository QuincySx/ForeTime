package com.smallraw.foretime.app.common.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;
import com.smallraw.foretime.app.common.widget.wheel.WheelPicker;

import java.util.ArrayList;
import java.util.List;

public class SelectLongTimeDialog extends BaseDialogView {
    private String mTitle;
    private String mUnit;
    private int mSelect;

    private TextView mTvTitle;
    private WheelPicker mViewWheel;
    private TextView mTvUnit;
    private OnWheelCallback mOnWheelCallback;

    private static List<String> hourList = new ArrayList<>();

    static {
        for (int i = 5; i <= 60; i++) {
            hourList.add(i + "");
        }
    }

    public SelectLongTimeDialog(Context context) {
        super(context);
    }

    private SelectLongTimeDialog(Builder builder) {
        super(builder.mContext);
        mTitle = builder.mTitle;
        mUnit = builder.mUnit;
        mSelect = builder.mSelect - 5;
        mOnWheelCallback = builder.mOnWheelCallback;

        if (mSelect >= 0 && mViewWheel != null) {
            mViewWheel.setSelectedItemPosition(mSelect, false);
        }
    }

    @Override
    protected View setRootView() {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_longtime_select, null, false);
        return inflate;
    }

    @Override
    protected void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        mViewWheel = findViewById(R.id.view_wheel);
        mTvUnit = findViewById(R.id.tv_unit);

        mViewWheel.setData(hourList);
        mViewWheel.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                if (mOnWheelCallback != null) {
                    mOnWheelCallback.onCall(hourList.get(position));
                }
            }
        });
    }

    public static final class Builder {
        private Context mContext;
        private String mTitle;
        private String mUnit;
        private int mSelect = -1;
        private OnWheelCallback mOnWheelCallback;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(String val) {
            mTitle = val;
            return this;
        }

        public Builder setUnit(String val) {
            mUnit = val;
            return this;
        }

        public Builder select(int val) {
            mSelect = val;
            return this;
        }

        public Builder setOnWheelCallback(OnWheelCallback onWheelCallback) {
            mOnWheelCallback = onWheelCallback;
            return this;
        }

        public SelectLongTimeDialog build() {
            return new SelectLongTimeDialog(this);
        }
    }
}

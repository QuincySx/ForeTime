/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.foretime.app.common.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;
import com.smallraw.foretime.app.common.widget.wheel.WheelPicker;

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
        super(builder);
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
        mViewWheel.setOnItemSelectedListener(
                new WheelPicker.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(WheelPicker picker, Object data, int position) {
                        if (mOnWheelCallback != null) {
                            mOnWheelCallback.onCall(hourList.get(position));
                        }
                    }
                });
    }

    public static final class Builder extends BaseDialogView.Builder {
        private Context mContext;
        private String mTitle;
        private String mUnit;
        private int mSelect = -1;
        private OnWheelCallback mOnWheelCallback;

        public Builder(Context context) {
            super(context);
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

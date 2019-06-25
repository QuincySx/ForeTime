package com.smallraw.foretime.app.common.widget.dialog;

import android.content.Context;
import android.view.View;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;

import org.jetbrains.annotations.NotNull;

/**
 * 添加倒数日任务选择器
 */
public class SelectDayTypeDialog extends BaseDialogView {
    private OnBaseDialogClickCallback mOnClickCallback;

    public SelectDayTypeDialog(Builder builder) {
        super(builder);
        mOnClickCallback = builder.onClickCallback;
    }

    private SelectDayTypeDialog(Context context) {
        super(context);
    }

    @Override
    protected void initView() {

    }


    @Override
    protected View setRootView() {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_day_type_select, null, false);
        View matter = inflate.findViewById(R.id.layout_matter);
        View cumulative = inflate.findViewById(R.id.layout_cumulative);
        matter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickCallback != null) {
                    mOnClickCallback.onClick(SelectDayTypeDialog.this, 0);
                }
            }
        });
        cumulative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickCallback != null) {
                    mOnClickCallback.onClick(SelectDayTypeDialog.this, 1);
                }
            }
        });
        return inflate;
    }

    public interface OnBaseDialogClickCallback {
        void onClick(BaseDialogView view, int index);
    }

    public static class Builder extends BaseDialogView.Builder {
        private OnBaseDialogClickCallback onClickCallback;

        public Builder(@NotNull Context context) {
            super(context);
        }

        public Builder setOnClickCallback(OnBaseDialogClickCallback onClickCallback) {
            this.onClickCallback = onClickCallback;
            return this;
        }

        @NotNull
        @Override
        public SelectDayTypeDialog build() {
            return new SelectDayTypeDialog(this);
        }
    }
}

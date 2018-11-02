package com.smallraw.foretime.app.ui.dialog;

import android.content.Context;
import android.view.View;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;

public class SelectDayTypeDialog extends BaseDialogView {
    public SelectDayTypeDialog(Context context) {
        super(context);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected View setRootView() {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_day_type_select, null, false);
        return inflate;
    }
}

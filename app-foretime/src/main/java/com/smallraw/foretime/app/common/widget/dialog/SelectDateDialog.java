package com.smallraw.foretime.app.common.widget.dialog;

import android.content.Context;
import android.view.View;

import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;
import com.smallraw.foretime.app.common.widget.wheel.WheelDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectDateDialog extends BaseDialogView {

    private WheelDatePicker mWheelDatePicker;

    private OnDateWheelCallback mOnDateWheelCallback;
    private long mTime;
    private Calendar mCalendar = Calendar.getInstance();
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public SelectDateDialog(Context context) {
        super(context);
    }

    public SelectDateDialog(Builder builder) {
        super(builder);
        mOnDateWheelCallback = builder.mOnWheelCallback;
        mTime = builder.mTime;

        setCurrentTime(mTime);
    }

    private void setCurrentTime(long time) {
        mCalendar.setTimeInMillis(time);

        mWheelDatePicker.setSelectedYear(mCalendar.get(Calendar.YEAR));
        mWheelDatePicker.setSelectedMonth(mCalendar.get(Calendar.MONTH) + 1);
        mWheelDatePicker.setSelectedDay(mCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void initView() {
        mWheelDatePicker = findViewById(R.id.viewWheelDataPicker);

        mWheelDatePicker.setOnDateSelectedListener(new WheelDatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(WheelDatePicker picker, Date date) {
                if (mOnDateWheelCallback != null) {
                    mOnDateWheelCallback.onCall(date);
                }
            }
        });
    }

    @Override
    protected View setRootView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_date_select, null, false);
        return view;
    }

    public static final class Builder extends BaseDialogView.Builder {
        private Context mContext;
        private long mTime = System.currentTimeMillis();

        private OnDateWheelCallback mOnWheelCallback;

        public Builder(Context context) {
            super(context);
            mContext = context;
        }

        public SelectDateDialog.Builder setOnWheelCallback(OnDateWheelCallback onWheelCallback) {
            mOnWheelCallback = onWheelCallback;
            return this;
        }

        public SelectDateDialog.Builder setTime(long time) {
            mTime = time;
            return this;
        }

        public SelectDateDialog build() {
            return new SelectDateDialog(this);
        }
    }
}

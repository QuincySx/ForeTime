package com.smallraw.foretime.app.common.widget.dialog;

import android.content.Context;
import android.view.View;

import com.smallraw.foretime.app.App;
import com.smallraw.foretime.app.R;
import com.smallraw.foretime.app.base.BaseDialogView;
import com.smallraw.support.wheelcompat.LoopScrollListener;
import com.smallraw.support.wheelcompat.LoopView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SelectDateDialog extends BaseDialogView {
    private final static List<String> yearList = new ArrayList<>();
    private final static List<String> monthList = new ArrayList<>();
    private List<String> dayList;

    private LoopView mViewYear;
    private LoopView mViewMonth;
    private LoopView mViewDay;

    private String year = "";
    private String month = "";
    private String day = "";

    private OnDateWheelCallback mOnDateWheelCallback;
    private long mTime;
    private Calendar mCalendar = Calendar.getInstance();
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    static {
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                monthList.add("0" + i);
            } else {
                monthList.add(i + "");
            }
        }

        for (int i = 1950; i <= 2050; i++) {
            yearList.add(i + "");
        }
    }

    public SelectDateDialog(Context context) {
        super(context);
    }

    public SelectDateDialog(Builder builder) {
        super(builder.mContext);
        mOnDateWheelCallback = builder.mOnWheelCallback;
        mTime = builder.mTime;

        setCurrentTime(mTime);
    }

    private void setCurrentTime(long time) {
        mCalendar.setTimeInMillis(time);
        year = mCalendar.get(Calendar.YEAR) + "";
        month = mCalendar.get(Calendar.MONTH) + 1 + "";
        day = mCalendar.get(Calendar.DAY_OF_MONTH) + "";

        App.getInstance().getAppExecutors().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                initDay(mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                int yearIndex = yearList.indexOf(year);
                int monthIndex = monthList.indexOf(month);
                int dayIndex = dayList.indexOf(day);

                App.getInstance().getAppExecutors().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mViewYear.setDataList(yearList);
                        mViewMonth.setDataList(monthList);
                        mViewDay.setDataList(dayList);

                        mViewYear.setInitPosition(yearIndex);
                        mViewMonth.setInitPosition(monthIndex);
                        mViewDay.setInitPosition(dayIndex);
                    }
                });
            }
        });
    }

    private void setCurrentDay(long time) {
        mCalendar.setTimeInMillis(time);
        day = mCalendar.get(Calendar.DAY_OF_MONTH) + "";

        App.getInstance().getAppExecutors().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                initDay(mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                int dayIndex = dayList.indexOf(day);

                App.getInstance().getAppExecutors().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mViewDay.setDataList(dayList);

                        mViewDay.setInitPosition(dayIndex);
                    }
                });
            }
        });
    }

    private void initDay(int days) {
        if (dayList == null) {
            dayList = new ArrayList<>();
        } else {
            dayList.clear();
        }
        for (int i = 1; i <= days; i++) {
            if (i < 10) {
                dayList.add("0" + i);
            } else {
                dayList.add(i + "");
            }
        }
    }

    @Override
    protected void initView() {
        mViewYear = findViewById(R.id.view_year);
        mViewMonth = findViewById(R.id.view_month);
        mViewDay = findViewById(R.id.view_day);

        mViewYear.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                year = yearList.get(item);
                reCalculation();
                if (mOnDateWheelCallback != null) {
                    mOnDateWheelCallback.onCall(year, month, day);
                }
            }
        });

        mViewMonth.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                month = monthList.get(item);
                reCalculation();
                if (mOnDateWheelCallback != null) {
                    mOnDateWheelCallback.onCall(year, month, day);
                }
            }
        });

        mViewDay.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                day = dayList.get(item);
                reCalculation();
                if (mOnDateWheelCallback != null) {
                    mOnDateWheelCallback.onCall(year, month, day);
                }
            }
        });
    }

    private void reCalculation() {
        try {
            Date parse = mSimpleDateFormat.parse(year + "-" + month + "-" + day);
            setCurrentDay(parse.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected View setRootView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_date_select, null, false);
        return view;
    }

    public static final class Builder {
        private Context mContext;
        private long mTime = System.currentTimeMillis();

        private OnDateWheelCallback mOnWheelCallback;

        public Builder(Context context) {
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

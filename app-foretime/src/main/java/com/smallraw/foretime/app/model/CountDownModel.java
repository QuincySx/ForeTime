package com.smallraw.foretime.app.model;

import android.os.CountDownTimer;
import android.support.annotation.IntDef;

import com.smallraw.foretime.app.business.timer.CountDownManager;
import com.smallraw.foretime.app.business.timer.CountdownState;

import java.util.ArrayList;
import java.util.List;

public class CountDownModel implements CountDownManager.OnCountDownListener {
    /**
     * 工作中状态
     */
    public final static int WORKING = 1;
    /**
     * 休息状态
     */
    public final static int REPOSE = 2;


    @IntDef({WORKING, REPOSE})
    public @interface Status {
    }

    private static CountDownModel sCountDownModel = null;
    private boolean isAuto = true;
    private CountDownManager mCountDownManager = new CountDownManager(0, this);
    private List<OnCountDownListener> mOnCountDownListenerList = new ArrayList<>();

    @Status
    private int mCurrentStatus = WORKING;

    public static CountDownModel getInstance() {
        if (sCountDownModel == null) {
            synchronized (CountDownTimer.class) {
                if (sCountDownModel == null) {
                    sCountDownModel = new CountDownModel();
                }
            }
        }
        return sCountDownModel;
    }

    public void init(@Status int status) {
        mCurrentStatus = status;
        long time;
        switch (status) {
            default:
            case WORKING:
                time = (long) (1000 * 60 * 0.3);
                break;
            case REPOSE:
                time = (long) (1000 * 60 * 0.2);
                break;
        }
        init(status, time);
    }

    public void init(@Status int status, long time) {
        mCurrentStatus = status;
        mCountDownManager.setRunningTime(time);
        mCountDownManager.init();
    }

    public void start() {
        mCountDownManager.startRunning();
    }

    public void pause() {
        mCountDownManager.pauseRunning();
    }

    public void resume() {
        mCountDownManager.resumeRunning();
    }

    public void stop() {
        mCountDownManager.stopRunning();
    }

    @Status
    public int getCurrentStatus() {
        return mCurrentStatus;
    }

    @CountdownState
    public int getCountDownStatus() {
        return mCountDownManager.getCountDownState();
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void addOnCountDownListener(OnCountDownListener listener) {
        mOnCountDownListenerList.add(listener);
    }

    public void removeOnCountDownListener(OnCountDownListener listener) {
        mOnCountDownListenerList.remove(listener);
    }

    @Override
    public void onCall(int state, long totalTime, long lastTime) {
        for (OnCountDownListener countDownListener : mOnCountDownListenerList) {
            countDownListener.onCall(mCurrentStatus, state, totalTime, lastTime);
        }
    }

    public interface OnCountDownListener {
        public void onCall(@Status int state, @CountdownState int countdownState, long totalTime, long lastTime);
    }
}

package com.smallraw.foretime.app.business.timer;

public class CountDownManager {

    //工作时长
    private long mRunningTime = 1000 * 60 * 2;

    private long mUsedTime = 0;

    //刷新间隔
    private long mRefreshIntervalTime = 25;

    private ITimerSupport mCountDownTimer = null;

    private OnCountDownListener mOnCountDownListener = null;

    public CountDownManager(long runningTime) {
        mRunningTime = runningTime;
    }

    public CountDownManager(long runningTime, OnCountDownListener listener) {
        mRunningTime = runningTime;
        mOnCountDownListener = listener;
    }

    /**
     * 当前倒计时状态
     */
    @CountdownState
    private int mCountDownState = CountdownState.STATE_INIT;

    public void setCountDownListener(OnCountDownListener onInitListener) {
        mOnCountDownListener = onInitListener;
    }

    public void init() {
        mCountDownState = CountdownState.STATE_INIT;
        mUsedTime = mRunningTime;
        onCall(mCountDownState, mRunningTime, mUsedTime);
    }

    /**
     * 开始工作
     */
    public void startRunning() {
        if (mCountDownTimer != null) {
            mCountDownTimer.stop();
        }
        mCountDownState = CountdownState.STATE_RUNNING;
        mCountDownTimer = new CountDownTimerSupport(mRunningTime, mRefreshIntervalTime);
        mCountDownTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                mUsedTime = millisUntilFinished;
                onCall(mCountDownState, mRunningTime, mUsedTime);
            }

            @Override
            public void onFinish() {
                mCountDownState = CountdownState.STATE_RUNNING_FINISH;
                onCall(mCountDownState, mRunningTime, mUsedTime);
            }
        });

        mCountDownTimer.start();
    }

    public void pauseRunning() {
        mCountDownState = CountdownState.STATE_RUNNING_PAUSE;
        mCountDownTimer.pause();
        onCall(mCountDownState, mRunningTime, mUsedTime);
    }

    public void resumeRunning() {
        mCountDownState = CountdownState.STATE_RUNNING;
        mCountDownTimer.resume();
        onCall(mCountDownState, mRunningTime, mUsedTime);
    }

    public void stopRunning() {
        mCountDownTimer.stop();
    }

    public void setRunningTime(long runningTime) {
        mRunningTime = runningTime;
    }

    @CountdownState
    public int getCountDownState() {
        return mCountDownState;
    }

    public void onCall(final @CountdownState int state, final long totalTime, final long lastTime) {
        if (mOnCountDownListener != null) {
            mOnCountDownListener.onCall(state, totalTime, lastTime);
        }
    }

    public interface OnCountDownListener {
        public void onCall(@CountdownState int state, long totalTime, long lastTime);
    }
}

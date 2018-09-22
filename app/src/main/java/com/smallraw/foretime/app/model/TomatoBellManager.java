package com.smallraw.foretime.app.model;

import android.support.annotation.IntDef;

import com.smallraw.foretime.app.common.timer.AdvancedCountdownTimer;

import java.util.ArrayList;
import java.util.List;

public class TomatoBellManager {
  public final static int STATE_INIT = 0;
  public final static int STATE_WORKING = 1;
  public final static int STATE_WORKING_PAUSE = 2;
  public final static int STATE_WORKING_FINISH = 3;
  public final static int STATE_REST = 4;
  public final static int STATE_REST_PAUSE = 5;
  public final static int STATE_REST_FINISH = 6;
  public final static int STATE_STOP = 7;

  @IntDef({STATE_INIT, STATE_WORKING, STATE_WORKING_PAUSE, STATE_WORKING_FINISH, STATE_REST, STATE_REST_PAUSE, STATE_REST_FINISH, STATE_STOP})
  public @interface CountdownState {
  }

  private volatile static TomatoBellManager sTomatoBellManager = null;

  //工作时长
  private long mWorkingTime = 1000 * 60 * 25;

  //休息时长
  private long mRestTime = 1000 * 25 * 60;

  private long mUsedTime = 0;

  //刷新间隔
  private long mRefreshIntervalTime = 100;

  private AdvancedCountdownTimer mCountDownTimer = null;

  private List<OnCountDownListener> mOnCountDownListeners = new ArrayList<>();

  /**
   * 当前倒计时状态
   */
  @CountdownState
  private int mCountDownState = STATE_INIT;

  public static TomatoBellManager getInstance() {
    if (sTomatoBellManager == null) {
      synchronized (TomatoBellManager.class) {
        if (sTomatoBellManager == null) {
          sTomatoBellManager = new TomatoBellManager();
        }
      }
    }
    return sTomatoBellManager;
  }

  public void addCountDownListener(OnCountDownListener onInitListener) {
    mOnCountDownListeners.add(onInitListener);
  }

  public void init() {
    mCountDownState = STATE_INIT;
    onCall(mCountDownState, mWorkingTime, mWorkingTime);
  }

  /**
   * 开始工作
   */
  public void startWorking() {
    if (mCountDownTimer != null) {
      mCountDownTimer.cancel();
    }
    mCountDownState = STATE_WORKING;
    mCountDownTimer = new AdvancedCountdownTimer(mWorkingTime, mRefreshIntervalTime) {
      @Override
      protected void onTick(long millisUntilFinished, int percent) {
        mUsedTime = millisUntilFinished;
        onCall(mCountDownState, mWorkingTime, mUsedTime);
      }

      @Override
      public void onFinish() {
        mCountDownState = STATE_WORKING_FINISH;
        onCall(mCountDownState, mWorkingTime, mWorkingTime);
      }
    };
    mCountDownTimer.start();
  }

  public void pauseWorking() {
    mCountDownState = STATE_WORKING_PAUSE;
    mCountDownTimer.pause();
  }

  public void resumeWorking() {
    mCountDownState = STATE_WORKING;
    mCountDownTimer.resume();
  }

  /**
   * 开始休息
   */
  public void startRest() {
    if (mCountDownTimer != null) {
      mCountDownTimer.cancel();
    }
    mCountDownState = STATE_REST;
    mCountDownTimer = new AdvancedCountdownTimer(mRestTime, mRefreshIntervalTime) {
      @Override
      protected void onTick(long millisUntilFinished, int percent) {
        mUsedTime = millisUntilFinished;
        onCall(mCountDownState, mWorkingTime, mUsedTime);
      }

      @Override
      public void onFinish() {
        mCountDownState = STATE_REST_FINISH;
        onCall(mCountDownState, mWorkingTime, mWorkingTime);
      }
    };
    mCountDownTimer.start();
  }

  public void pauseRest() {
    mCountDownState = STATE_REST_PAUSE;
    mCountDownTimer.pause();
  }

  public void resumeRest() {
    mCountDownState = STATE_REST;
    mCountDownTimer.resume();
  }

  public void setWorkingTime(long workingTime) {
    mWorkingTime = workingTime;
  }

  public void setRestTime(long restTime) {
    mRestTime = restTime;
  }

  @CountdownState
  public int getCountDownState() {
    return mCountDownState;
  }

  public void onCall(final @CountdownState int state, final long totalTime, final long lastTime) {
    for (OnCountDownListener countDownListener : mOnCountDownListeners) {
      countDownListener.onCall(state, totalTime, lastTime);
    }
  }

  public interface OnCountDownListener {
    public void onCall(@CountdownState int state, long totalTime, long lastTime);
  }
}

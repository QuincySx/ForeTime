package com.smallraw.foretime.app.common.timer;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class ExtCountdownTimer {
  /**
   * Millis since epoch when alarm should stop.
   */
  private final long mMillisInFuture;

  /**
   * The interval in millis that the user receives callbacks
   */
  private final long mCountdownInterval;

  private long mStopTimeInFuture;

  /**
   * boolean representing if the timer was cancelled
   */
  private boolean mCancelled = false;
  private boolean mPauseed = false;

  /**
   * @param millisInFuture    The number of millis in the future from the call
   *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
   *                          is called.
   * @param countDownInterval The interval along the way to receive
   *                          {@link #onTick(long)} callbacks.
   */
  public ExtCountdownTimer(long millisInFuture, long countDownInterval) {
    mMillisInFuture = millisInFuture;
    mCountdownInterval = countDownInterval;
  }

  /**
   * Cancel the countdown.
   */
  public synchronized final void cancel() {
    mCancelled = true;
    mHandler.removeMessages(MSG);
  }

  /**
   * Pause the countdown.
   */
  public synchronized final void pause() {
    mPauseed = true;
  }

  /**
   * Resume the countdown.
   */
  public synchronized final void resume() {
    mPauseed = false;
  }

  /**
   * Start the countdown.
   */
  public synchronized final ExtCountdownTimer start() {
    mCancelled = false;
    if (mMillisInFuture <= 0) {
      onFinish();
      return this;
    }
    mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
    mHandler.sendMessage(mHandler.obtainMessage(MSG));
    return this;
  }


  /**
   * Callback fired on regular interval.
   *
   * @param millisUntilFinished The amount of time until finished.
   */
  public abstract void onTick(long millisUntilFinished);

  /**
   * Callback fired when the time is up.
   */
  public abstract void onFinish();


  private static final int MSG = 1;


  // handles counting down
  @SuppressLint("HandlerLeak")
  private Handler mHandler = new Handler() {

    @Override
    public void handleMessage(Message msg) {

      synchronized (ExtCountdownTimer.this) {
        if (mCancelled) {
          return;
        }

        final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

        if (mPauseed) {
          mStopTimeInFuture += mCountdownInterval;
          sendMessageDelayed(obtainMessage(MSG), mCountdownInterval);
          return;
        }

        if (millisLeft <= 0) {
          onFinish();
        } else {
          long lastTickStart = SystemClock.elapsedRealtime();
          onTick(millisLeft);

          // take into account user's onTick taking time to execute
          long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
          long delay;

          if (millisLeft < mCountdownInterval) {
            // just delay until done
            delay = millisLeft - lastTickDuration;

            // special case: user's onTick took more than interval to
            // complete, trigger onFinish without delay
            if (delay < 0) delay = 0;
          } else {
            delay = mCountdownInterval - lastTickDuration;

            // special case: user's onTick took more than interval to
            // complete, skip to next interval
            while (delay < 0) delay += mCountdownInterval;
          }

          sendMessageDelayed(obtainMessage(MSG), delay);
        }
      }
    }
  };
}

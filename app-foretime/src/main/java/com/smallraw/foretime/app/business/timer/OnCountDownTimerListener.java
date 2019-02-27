package com.smallraw.foretime.app.business.timer;

public interface OnCountDownTimerListener {
    void onTick(long millisUntilFinished);

    void onFinish();
}

package com.smallraw.foretime.app.business.timer;

public interface ITimerSupport {

    void start();

    void pause();

    void resume();

    void stop();

    void reset();

    void setOnCountDownTimerListener(OnCountDownTimerListener listener);
}

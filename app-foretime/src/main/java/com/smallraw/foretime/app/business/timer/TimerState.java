package com.smallraw.foretime.app.business.timer;

import android.support.annotation.IntDef;

@IntDef({TimerState.START, TimerState.PAUSE, TimerState.FINISH})
public @interface TimerState {
    public static final int START = 0;
    public static final int PAUSE = 1;
    public static final int FINISH = 2;
}

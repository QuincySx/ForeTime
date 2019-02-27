package com.smallraw.foretime.app.business.timer;

import android.support.annotation.IntDef;

@IntDef({CountdownState.STATE_INIT, CountdownState.STATE_RUNNING, CountdownState.STATE_RUNNING_PAUSE, CountdownState.STATE_RUNNING_FINISH})
public @interface CountdownState {
    public final static int STATE_INIT = 0;
    public final static int STATE_RUNNING = 1;
    public final static int STATE_RUNNING_PAUSE = 2;
    public final static int STATE_RUNNING_FINISH = 3;
}
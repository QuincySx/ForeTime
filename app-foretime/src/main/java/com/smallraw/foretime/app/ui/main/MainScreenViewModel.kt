package com.smallraw.foretime.app.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

@IntDef(MainPageIndex.TOMATO_BELL, MainPageIndex.CALENDAR)
annotation class MainPageIndex {
    companion object {
        const val TOMATO_BELL = 0
        const val CALENDAR = 1
    }
}

class MainScreenViewModel : ViewModel() {
    var mTomatoBellSuspensionButtonResource = MutableLiveData<@DrawableRes Int>()
    var mCalendarSuspensionButtonResource = MutableLiveData<@DrawableRes Int>()
    var mMainPageIndex = MutableLiveData<@MainPageIndex Int>(MainPageIndex.TOMATO_BELL)
}
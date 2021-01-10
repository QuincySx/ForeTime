package com.smallraw.foretime.app.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.databinding.ObservableField
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
    var tomatoBellSuspensionRes = ObservableField<@DrawableRes Int>()
    var calendarSuspensionRes = ObservableField<@DrawableRes Int>()
    var mainPageIndex = MutableLiveData<@MainPageIndex Int>(MainPageIndex.TOMATO_BELL)
}
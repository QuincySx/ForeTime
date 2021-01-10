package com.smallraw.foretime.app.viewmodle

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {
    val isPlay = ObservableField<Boolean>()
    val name = ObservableField<String>()

    init {
        name.set("夏天的雷雨")
        isPlay.set(true)
    }
}
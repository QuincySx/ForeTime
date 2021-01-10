package com.smallraw.foretime.app.ui.main.tomatoBell

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R

class TomatoBellViewModel : ViewModel() {
    val timeWrapper = ObservableField("00:00")
    val progress = ObservableField(1F)
    val progressColor = ObservableField(App.getInstance().getColor(R.color.WorkingProgessColor))
    val operationHintsVisibility = ObservableField(false)
    val operationHints = ObservableField("")
    val touchTimeProgressVisibility = ObservableField(false)
    val touchTimeProgress = ObservableField(0F)
}

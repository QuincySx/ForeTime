/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

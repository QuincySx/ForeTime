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
package com.smallraw.foretime.app.base.databinding

import android.util.SparseArray
import androidx.lifecycle.ViewModel

class DataBindingConfig(
    private val layout: Int,
    private val vmVariableId: Int? = null,
    private val stateViewModel: ViewModel? = null
) {
    private val bindingParams: SparseArray<Any> = SparseArray()

    fun getLayout(): Int {
        return layout
    }

    fun getVmVariableId(): Int? {
        return vmVariableId
    }

    fun getStateViewModel(): ViewModel? {
        return stateViewModel
    }

    fun getBindingParams(): SparseArray<Any> {
        return bindingParams
    }

    fun addBindingParam(
        variableId: Int,
        anyParam: Any
    ): DataBindingConfig {
        if (bindingParams[variableId] == null) {
            bindingParams.put(variableId, anyParam)
        }
        return this
    }
}

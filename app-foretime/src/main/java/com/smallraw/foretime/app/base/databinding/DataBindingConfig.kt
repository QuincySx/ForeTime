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
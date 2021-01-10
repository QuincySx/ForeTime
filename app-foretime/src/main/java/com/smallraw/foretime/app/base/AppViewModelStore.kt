package com.smallraw.foretime.app.base

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

interface IViewModelStoreApp {
    fun getViewModelStore(): ViewModelStore
}

class AppViewModelStore : ViewModelStoreOwner {

    private var mAppViewModelStore: ViewModelStore? = null

    init {
        mAppViewModelStore = ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore!!
    }
}
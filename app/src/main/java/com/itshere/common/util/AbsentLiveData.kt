package com.itshere.common.util

import android.arch.lifecycle.LiveData

class AbsentLiveData<T> private constructor() : LiveData<T>() {

    companion object {
        fun <T> create(): AbsentLiveData<T> = AbsentLiveData()
    }

    init {
        postValue(null)
    }
}
package com.itshere.network

import com.itshere.network.Status.ERROR
import com.itshere.network.Status.LOADING
import com.itshere.network.Status.SUCCESS

data class Resource<T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T) = Resource(SUCCESS, data, null)
        fun <T> error(message: String, data: T?) = Resource(ERROR, data, message)
        fun <T> loading(data: T?) = Resource(LOADING, data, null)
    }
}
package com.itshere.common.util

class Optional<out T> private constructor(val value: T?) {

    companion object {
        fun <T> empty() = Optional<T>(null)
        fun <T> of(value: T) = Optional(value)
    }
}
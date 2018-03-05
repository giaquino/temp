package com.itshere.common.util

import android.content.Intent
import android.view.View

fun View.startActivity(intent: Intent) {
    this.context.startActivity(intent)
}

infix fun Any?.checkNotNull(message: String) {
    if (this == null) throw KotlinNullPointerException(message)
}